package com.claro.cron.ws.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.claro.cron.ws.binding.Entry;
import com.claro.cron.ws.binding.RESULTADO;
import com.claro.cron.ws.binding.RESULTS;
import com.claro.cron.ws.binding.RmdSelect;
import com.claro.cron.ws.binding.RmdUpdate;

@Service
@Qualifier(value = "closeTicklerService")
@Transactional
public class RemedyClientImpl implements RemedyClient {

	@Value("${remedy.ws.host}")
	private String host;

	@Value("${remedy.http.timeout}")
	private String timeout;

	@Value("${remedy.http.tries}")
	private String httpTry;

	@Value("${remedy.pais}")
	private String pais;

	@PersistenceContext
	private EntityManager sgtEntityManager;

	private static Logger logger = LoggerFactory.getLogger(RemedyClientImpl.class);

	String selectUrl = "RMDSelect?cSistema=TEMIPARG&cForma=HPD:Help%20Desk"
			+ "&cColumnas=7%201000000161%201000000000%208%20301572100&cCondiciones=%277%27=%27(4,5)%27%20%27301572100%27=%27PARAMIO%27"+
			"%20%271000000019%27=%27CUENTA%20GENERICA%27%20%271000000018%27=%27STEALTH%20ARG%27%20%271000000251%27=%27CLARO%20ARGENTINA%27"+
			"%20%276%27>=%27CANT%27"; // ESTADO

	String updateUrl = "RMDUpdate?cSistema=TEMIPARG&cForma=HPD:Help%20Desk&cID=PARAMIO1&cColumnas=%27301572100%27=%27PARAMIO2%27";

	int reint = 1;

	String obtenerDias = "SELECT trunc(STL_VALUE) FROM STL_PARAMETERS WHERE STL_ID='JVTREM'";
	
	public String getDate() throws Exception {
		Calendar calendar = Calendar.getInstance();
		String date = "";
		Date fecha = new Date();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe

		StatelessSession stateless = null;
		try {

			Session session = sgtEntityManager.unwrap(Session.class);
			SessionFactory sessionFactory = session.getSessionFactory();
			stateless = sessionFactory.openStatelessSession();
		
			NativeQuery<?> nq = stateless.createNativeQuery(obtenerDias).setCacheable(false).setReadOnly(true)
					.setFetchSize(10);

			ScrollableResults filas = nq.scroll(ScrollMode.FORWARD_ONLY);
			
			if ((filas != null)&&(filas.next())) {
				calendar.add(Calendar.DAY_OF_YEAR, (new Integer(filas.get(0).toString())) * -1);
				
				SimpleDateFormat mdyFormat = new SimpleDateFormat("dd/MM/yyyy");
				date = mdyFormat.format(calendar.getTime());
				logger.info("Fecha de consulta " + date);
			} else {
				throw new Exception("Parametro fecha no definido");
			}

		} catch (Exception e) {
			throw new Exception("Error en calculo de fecha " + e.getMessage());
		} finally {
			if (stateless != null)
				stateless.close();
		}
		return date;
	}

	@Override
	public String[] rmdSelectByStatus(String status) {

		logger.info(":::: Barrer remedy ::::");
		int intentos = Integer.parseInt(httpTry);

		String urlItem = host + selectUrl.replace("PARAMIO", status + pais.toUpperCase());
			
		URL request_url;
		RmdSelect select = null;

		try {
			urlItem = urlItem.replace("CANT", getDate());
			JAXBContext jaxbContext = JAXBContext.newInstance(RmdSelect.class);
			logger.debug("url: " + urlItem);
			request_url = new URL(urlItem);

			logger.info("Conexion a Remedy - Intento " + reint);
			HttpURLConnection http_conn = (HttpURLConnection) request_url.openConnection();
			http_conn.setConnectTimeout(Integer.parseInt(timeout));
			http_conn.setReadTimeout(Integer.parseInt(timeout));
			http_conn.setRequestProperty("Accept-Charset", "UTF-8");

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			select = (RmdSelect) jaxbUnmarshaller.unmarshal(http_conn.getInputStream());

			RESULTADO resultado = select.getRESULTADO();
			
			List<Entry> listaEntry = resultado.getEntry();
			
			if ((listaEntry != null) && (listaEntry.size() > 0)) {
				reint = 1;
				logger.info("Consulta a Remedy trajo " + listaEntry.size() + " incidentes");
				procesarIncidentesRem(listaEntry);
			} else {
				logger.info("::::: Consulta no devuelve incidentes :::::");
			}

		} catch (MalformedURLException e) {
			logger.error("Error URL RMDSELECT: " + e.getMessage());
		} catch (IOException e) {
			logger.error("Error IO RMDSELECT: " + e.getMessage());
			reint++;
			if ((e.getMessage().contains("timed out")) && (reint <= intentos)) {
				this.rmdSelectByStatus(status);
			}
		} catch (JAXBException e) {
			logger.error("Error JAXB RMDSELECT: " + e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	LinkedHashMap<String, String> incidenteLista = new LinkedHashMap<>();
	String key = "";
	String value = "";

	public void procesarIncidentesRem(List<Entry> listaEntry) {

		listaEntry.forEach((entry) -> {

			entry.getField().forEach((field) -> {
				if ("1000000161".equals(field.getId())) // nro incidente
				{
					key = field.getValue();
				}
				if ("1000000000".equals(field.getId())) // DESCRIPCION INCIDENTE
				{
					value = field.getValue();
				}
			});

			if (!key.isEmpty())
				incidenteLista.put(key, value);
		});
		
		if (incidenteLista.size() > 0) {
			procesarTicklers(incidenteLista);
		} else {
			logger.info("Los incidentes no son procesables...");
		}
	}

	public void procesarTicklers(LinkedHashMap<String, String> incidentesRem) {

		Set<?> set = incidentesRem.entrySet();

		Iterator<?> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry item = (Map.Entry) iterator.next();
			matchearTicklerTicket((String) item.getKey(), (String) item.getValue());
		}
	}

	private static String matchQuery = "select tso_tck_id,tso_remedy_id,tso_nim_number,tso_state from "
			+ "TICKLERS_SOLING where tso_remedy_id =:p";

	public boolean matchearTicklerTicket(String ticketRemId, String ticketDesc) {

		StatelessSession stateless = null;
		boolean resp = false;
		boolean hasResults = false;
		try {

			Session session = sgtEntityManager.unwrap(Session.class);
			SessionFactory sessionFactory = session.getSessionFactory();
			stateless = sessionFactory.openStatelessSession();

			NativeQuery<?> nq = stateless.createNativeQuery(matchQuery).setParameter("p", ticketRemId)
					.setCacheable(false).setReadOnly(true).setFetchSize(10);

			ScrollableResults filas = nq.scroll(ScrollMode.FORWARD_ONLY);

			if (filas != null) {
				while (filas.next()) {
					hasResults = true;
					if (!"E".equals(filas.get(3).toString())) {
						logger.info("::: Matcheo con soling, incidente: " + ticketRemId + ", insertar hijo");
						insertarTickler(ticketRemId, ticketDesc, (BigDecimal) filas.get(0), (String) filas.get(2));
					} else {
						logger.info(
								"::: Matcheo con soling, incidente: " + ticketRemId + ", realizar update en remedy");
						// Ya se intento antes hacer update en remedy, no se
						// pudo, intentamos de nuevo
						if ("OK".equals(rmdUpdateStatus(ticketRemId, "P" + pais.toUpperCase()))) {
							borrarTicklerSolingPadre((BigDecimal) filas.get(0));
						}
					}
				}

			} else {
				logger.info("No hubo matcheo con STEALTH");
			}

		} catch (Exception e) {
			logger.error("Error - Matcheo tickler-ticket " + e.getMessage());
		} finally {
			if (!hasResults) {
				logger.info("No hubo matcheo " + ticketRemId);
			} else {
				hasResults = false;
			}
			if (stateless != null)
				stateless.close();
		}
		return resp;
	}

	public String insertarTickler(String ticketId, String ticketDesc, BigDecimal ticklerId, String nim)
			throws Exception {
		StatelessSession stateless = null;
		try {
			Session session = sgtEntityManager.unwrap(Session.class);
			SessionFactory sessionFactory = session.getSessionFactory();
			stateless = sessionFactory.openStatelessSession();

			StoredProcedureQuery queryIns = stateless.createStoredProcedureCall("SAP_INGE_ANSWER")
					.registerStoredProcedureParameter("mngrp", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("fetxt", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("rsn_id", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("txline", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("qmnum", String.class, ParameterMode.IN)
					.registerStoredProcedureParameter("tck", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("ret", String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("msg", String.class, ParameterMode.OUT)

					.setParameter("mngrp", "SOLING")
					.setParameter("fetxt", nim.toString())
					.setParameter("rsn_id", "TOS")
					.setParameter("txline", ticketDesc)
					.setParameter("qmnum", ticklerId.toString());

			queryIns.execute();

			String resp = (String) queryIns.getOutputParameterValue("ret");
			logger.info("::: Respuesta insercion tickler hijo: " + resp);

			if ("OK".equals(resp)) // Inserci�n de tickler nuevo da ok
			{
				if ("OK".equals(rmdUpdateStatus(ticketId, "P" + pais.toUpperCase()))) // Si update da ok
				{
					// Borrar tickler padre en TICKLER_SOLING
					borrarTicklerSolingPadre(ticklerId);
				} else { // update en remedy da error
					// Poner tickler_soling en estado E
					updateEstadoTicklerSolingPadre(ticklerId, "E");
				}
			} else {
				throw new Exception("No se pudo insertar tickler de cierre");
			}

		} catch (Exception e) {
			logger.error("Error - Insertar Tickler - " + e.getMessage());
			throw e;
		} finally {
			if (stateless != null)
				stateless.close();
		}
		return "";
	}

	@Override
	public String rmdUpdateStatus(String incident, String status) {

		String urlItem = updateUrl.replace("PARAMIO1", incident).replace("PARAMIO2", status);
		urlItem = host + urlItem;
		URL request_url;
		RmdUpdate select = null;

		String resp = "FAIL";
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(RmdUpdate.class);
			request_url = new URL(urlItem);

			HttpURLConnection http_conn = (HttpURLConnection) request_url.openConnection();
			http_conn.setConnectTimeout(Integer.parseInt(timeout));
			http_conn.setReadTimeout(Integer.parseInt(timeout));

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			select = (RmdUpdate) jaxbUnmarshaller.unmarshal(http_conn.getInputStream());
			logger.info("::: Procesando resultado update.....");

			RESULTS resultado = select.getRESULTS();
			List<String> listaEntry = resultado.getUpdated();
			if ((listaEntry != null) && (listaEntry.size() > 0)) {
				String respU = new String(listaEntry.get(0));
				logger.info("Resultado de update en remedy: " + respU);
				return respU.toUpperCase(); // OK
			} else {
				logger.info("No se pudo actualizar ticket a estado " + status);
			}

		} catch (MalformedURLException e) {
			logger.error("Error MalFormed URL update de ticket remedy " + e.getMessage());
		} catch (IOException e) {
			logger.error("Error I/O update de ticket remedy " + e.getMessage());
		} catch (JAXBException e) {
			logger.error("Error JAX update de ticket remedy " + e.getMessage());
		}

		return resp;
	}

	private String deleteQuery = "delete from TICKLERS_SOLING where TSO_TCK_ID =";

	public void borrarTicklerSolingPadre(BigDecimal ticklerId) {
		StatelessSession stateless = null;
		
		try {
			Session session = sgtEntityManager.unwrap(Session.class);
			SessionFactory sessionFactory = session.getSessionFactory();
			stateless = sessionFactory.openStatelessSession();
			String deleteQueryTmp = deleteQuery + "'" + ticklerId + "'";
			Transaction tx = stateless.beginTransaction();
			int a = stateless.createNativeQuery(deleteQueryTmp).executeUpdate();
			tx.commit();
			logger.info(":::: Resultado borrar soling padre:" + ticklerId + ", es: " + (a > 0 ? "exitoso" : "fallido"));

		} catch (Exception e) {
			logger.error(":::: Error borrar soling padre " + e.getLocalizedMessage());
			throw e;
		} finally {
			if (stateless != null)
				stateless.close();
		}

	}

	private String updateQuery = "update TICKLERS_SOLING SET TSO_STATE = :s where TSO_TCK_ID = :t";

	public void updateEstadoTicklerSolingPadre(BigDecimal ticklerId, String estado) {
		StatelessSession stateless = null;
		try {
			Session session = sgtEntityManager.unwrap(Session.class);
			SessionFactory sessionFactory = session.getSessionFactory();
			stateless = sessionFactory.openStatelessSession();

			Transaction tx = stateless.beginTransaction();
			int upddateRes = stateless.createNativeQuery(updateQuery).setParameter("s", estado)
					.setParameter("t", ticklerId).executeUpdate();
			tx.commit();

			if (upddateRes == 1) {
				logger.info("Se actualiza el tickler soling con estado " + estado);
			} else {
				logger.info("No se pudo actualizar tickler soling con estado " + estado);
			}

		} catch (Exception e) {
			logger.error("Error - updateEstadoTickler padre " + e.getMessage());
			throw e;
		} finally {
			if (stateless != null)
				stateless.close();
		}

	}

}
