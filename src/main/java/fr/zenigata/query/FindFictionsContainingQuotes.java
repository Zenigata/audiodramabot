package fr.zenigata.query;

import java.util.List;

import com.sybit.airtable.Query;
import com.sybit.airtable.Sort;

import fr.zenigata.util.QueryUtils;

public class FindFictionsContainingQuotes implements Query {

  @Override
  public String filterByFormula() {
    return null;
  }

  @Override
  public String[] getFields() {
    return new String[] { QueryUtils.FIELD_RECORD_ID };
  }

  @Override
  public Integer getMaxRecords() {
    return null;
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
    return "Citations";
  }

}
