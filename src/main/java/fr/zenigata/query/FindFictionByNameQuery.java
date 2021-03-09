package fr.zenigata.query;

import java.util.List;

import com.sybit.airtable.Query;
import com.sybit.airtable.Sort;

public class FindFictionByNameQuery implements Query {

  private String parameter;

  public FindFictionByNameQuery(String parameter) {
    this.parameter = parameter;
  }

  @Override
  public String filterByFormula() {
    return "SEARCH(LOWER(\"" + parameter + "\"), LOWER(Nom)) > 0";
  }

  @Override
  public String[] getFields() {
    return null;
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
