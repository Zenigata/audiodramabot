package fr.zenigata;

import java.util.List;

import com.sybit.airtable.Base;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryUtils {

  private static final Logger logger = LoggerFactory.getLogger(QueryUtils.class);

  public static List<Fiction> retrieveAllFictions(Base base) throws HttpResponseException, AirtableException {
    String[] fields = { "Record ID" };
    List<Fiction> allFictions = base.table(Bot.TABLE_FICTION, Fiction.class).select(fields);
    logger.debug("Il y a {} fictions", allFictions.size());
    return allFictions;
  }

}
