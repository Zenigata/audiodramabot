package fr.zenigata.query;

import java.util.List;

import com.sybit.airtable.Query;
import com.sybit.airtable.Sort;

import fr.zenigata.util.QueryUtils;

public class FindEpisodeByNameQuery implements Query {

  private String parameter;

  public FindEpisodeByNameQuery(String parameter) {
    this.parameter = parameter;
  }

  @Override
  public String filterByFormula() {
    return "\"" + parameter + "\" = " + QueryUtils.FIELD_PATH;
  }

  @Override
  public String[] getFields() {
    return new String[] { QueryUtils.FIELD_NOM, QueryUtils.FIELD_PISTE, QueryUtils.FIELD_DUREE, QueryUtils.FIELD_PATH };
  }

  @Override
  public Integer getMaxRecords() {
    return 1;
  }

  @Override
  public String getOffset() {
    return null;
  }

  @Override
  public Integer getPageSize() {
    return null;
  }

  @Override
  public List<Sort> getSort() {
    return null;
  }

  @Override
  public String getView() {
    return null;
  }

}
