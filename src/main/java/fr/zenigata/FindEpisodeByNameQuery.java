package fr.zenigata;

import java.util.List;

import com.sybit.airtable.Query;
import com.sybit.airtable.Sort;

public class FindEpisodeByNameQuery implements Query {

  private String parameter;

  public FindEpisodeByNameQuery(String parameter) {
    this.parameter = parameter;
  }

  @Override
  public String filterByFormula() {
    return "\"" + parameter + "\" = Path";
  }

  @Override
  public String[] getFields() {
    return new String[] { "Nom", "Piste", "Durée", "Path" };
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
