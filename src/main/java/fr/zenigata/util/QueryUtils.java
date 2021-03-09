package fr.zenigata.util;

import java.util.List;

import com.sybit.airtable.Base;
import com.sybit.airtable.Query;
import com.sybit.airtable.exception.AirtableException;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.zenigata.CommandManager;
import fr.zenigata.data.Fiction;
import fr.zenigata.query.FindFictionByNameQuery;
import fr.zenigata.query.FindFictionsByGenreQuery;

public class QueryUtils {

  public static final String TABLE_FICTION = "Fiction";
  public static final String FIELD_RECORD_ID = "Record ID";
  public static final String FIELD_NOM = "Nom";
  public static final String FIELD_PISTE = "Piste";
  public static final String FIELD_DUREE = "Durée";
  public static final String FIELD_PATH = "Path";
  public static final String FIELD_GENRE = "Genre";
  public static final String FIELD_AUTEUR = "Auteur";

  private static final Logger logger = LoggerFactory.getLogger(QueryUtils.class);

  public static List<Fiction> retrieveAllFictions(Base base) throws HttpResponseException, AirtableException {
    String[] fields = { FIELD_RECORD_ID };
    List<Fiction> allFictions = base.table(TABLE_FICTION, Fiction.class).select(fields);
    logger.debug("Il y a {} fictions", allFictions.size());
    return allFictions;
  }

  public static List<Fiction> retrieveAllFictionsByGenre(Base base, String genre)
      throws HttpResponseException, AirtableException {
    Query query = new FindFictionsByGenreQuery(genre);
    List<Fiction> allFictions = base.table(TABLE_FICTION, Fiction.class).select(query);
    logger.debug("Il y a {} fictions", allFictions.size());
    return allFictions;
  }

  public static List<Fiction> findFictionWithName(String name) throws AirtableException {
    Query query = new FindFictionByNameQuery(name);
    List<Fiction> found = CommandManager.getInstance().getBase().table(TABLE_FICTION, Fiction.class).select(query);
    return found;
  }

}
