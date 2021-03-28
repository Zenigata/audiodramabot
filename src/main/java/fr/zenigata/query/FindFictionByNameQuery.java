package fr.zenigata.query;

import java.util.Arrays;
import java.util.List;

import com.sybit.airtable.Query;
import com.sybit.airtable.Sort;
import com.sybit.airtable.Sort.Direction;

import fr.zenigata.util.QueryUtils;

public class FindFictionByNameQuery implements Query {

  private String parameter;

  public FindFictionByNameQuery(String parameter) {
    this.parameter = parameter;
  }

  @Override
  public String filterByFormula() {
    return "SEARCH(LOWER(\"" + parameter + "\"), LOWER(" + QueryUtils.FIELD_NOM + ")) > 0";
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
    return Arrays.asList(new Sort("Longueur", Direction.asc));
  }

  @Override
  public String getView() {
    return null;
  }

}
