package com.techmahindra.vehicletelemetry.chart;
// Copyright 2009 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import com.google.common.collect.Lists;
import com.google.visualization.datasource.Capabilities;
import com.google.visualization.datasource.DataSourceHelper;
import com.google.visualization.datasource.DataSourceRequest;
import com.google.visualization.datasource.QueryPair;
import com.google.visualization.datasource.base.DataSourceException;
import com.google.visualization.datasource.base.ReasonType;
import com.google.visualization.datasource.base.ResponseStatus;
import com.google.visualization.datasource.base.StatusType;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.query.AbstractColumn;
import com.google.visualization.datasource.query.Query;
import com.techmahindra.vehicletelemetry.rest.bean.CarData;
import com.techmahindra.vehicletelemetry.rest.dao.CarDataDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * A demo servlet for serving a data table from a data source that has query capabilities.
 * This data source has a native 'select' capability.
 * Other parts of the query are handled by the completion query.
 *
 * The data source can return two different data tables, depending on the
 * tableId parameter. By default the data source returns the 'animals' data table, unless the
 * tableId parameter is set to 'planets' in which case the data source returns the 'planets' data 
 * table.
 *
 * For the sake of a complete example this servlet extends HttpServlet.
 * DataSourceServlet is an abstract class that provides a template behavior for
 * serving data source requests. Consider extending DataSourceServlet for
 * easier implementation.
 *
 * @see com.google.visualization.datasource.DataSourceServlet
 * @see com.google.visualization.datasource.example.AdvancedExampleServlet
 *
 * @author Eran W.
 */
public class CarEventServlet extends HttpServlet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
   * The log used throughout the data source library.
   */
  private static final Log log = LogFactory.getLog(CarEventServlet.class.getName());
  
  /**
   * An enum of planets. Each planet has a name, surface gravity and 
   * number of moons.
   */
  private class Planet {
    
	/*JUPITER ("Jupi",317.8, 11.2, 63),
    SATURN  ("Satur",95.2, 9.4, 60),
    URANUS  ("Uran",14.5, 4, 27),
    NEPTUNE ("Nept",17.2, 3.9, 13);*/
	  private final String name;
    private final double mass;
    private final String model;
    private final double surfaceGravity;
    private final int numberOfMoons;

    private Planet(String name, String model,double mass, double surfaceGravity, int numberOfMoons) {
      this.name = name;
      this.mass = mass;
      this.model = model;
      this.surfaceGravity = surfaceGravity;
      this.numberOfMoons = numberOfMoons;
    }

    public String getName() {
        return name;
      }
    
    public String getModel() {
        return model;
      }
    public double getMass() {
      return mass;
    }

    public double getSurfaceGravity() {
      return surfaceGravity;
    }

    public int getNumberOfMoons() {
      return numberOfMoons;
    }
  }
  
  private static class CarEvent {
	    
		/*JUPITER ("Jupi",317.8, 11.2, 63),
	    SATURN  ("Satur",95.2, 9.4, 60),
	    URANUS  ("Uran",14.5, 4, 27),
	    NEPTUNE ("Nept",17.2, 3.9, 13);*/
		private final String city;
	    private final String model;
	    private final String count;
	    
	    
	    private CarEvent(String city, String model,String count) {
	      this.city = city;
	      this.count = count;
	      this.model = model;
	    }

		public String getCity() {
			return city;
		}

		public String getModel() {
			return model;
		}

		public String getCount() {
			return count;
		}
	   
  }
  
  
  

  private static final String PLANET_COLUMN = "name";
  private static final String MODEL_COLUMN = "model";
  private static final String MASS_COLUMN = "mass";
  private static final String GRAVITY_COLUMN = "gravity";
  private static final String MOONS_COLUMN = "moons";

  private static final ColumnDescription[] planetTableColumns =
      new ColumnDescription[] {
        new ColumnDescription(PLANET_COLUMN, ValueType.TEXT, "Planet"),
        new ColumnDescription(MODEL_COLUMN, ValueType.TEXT, "Model"),
        new ColumnDescription(MASS_COLUMN, ValueType.NUMBER, "Mass"),
        new ColumnDescription(GRAVITY_COLUMN, ValueType.NUMBER, "Surface Gravity"),
        new ColumnDescription(MOONS_COLUMN, ValueType.NUMBER, "Number of Moons")
  };
  
  
  ///car table
  
  private static final String CITY_COLUMN = "city";
  private static final String MODEL1_COLUMN = "model";
  private static final String COUNT_COLUMN = "count";
  /*private static final String GRAVITY_COLUMN = "gravity";
  private static final String MOONS_COLUMN = "moons";*/

  private static final ColumnDescription[] carTableColumns =
      new ColumnDescription[] {
        new ColumnDescription(CITY_COLUMN, ValueType.TEXT, "city"),
        new ColumnDescription(MODEL1_COLUMN, ValueType.NUMBER, "Model"),
        new ColumnDescription(COUNT_COLUMN, ValueType.NUMBER, "count"),
        /*new ColumnDescription(GRAVITY_COLUMN, ValueType.NUMBER, "Surface Gravity"),
        new ColumnDescription(MOONS_COLUMN, ValueType.NUMBER, "Number of Moons")*/
  };
  

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    DataSourceRequest dsRequest = null;

    try {
      // Extract the request parameters.
      dsRequest = new DataSourceRequest(req);

      // NOTE: If you want to work in restricted mode, which means that only
      // requests from the same domain can access the data source, you should
      // uncomment the following call.
      //
      // DataSourceHelper.verifyAccessApproved(dsRequest);

      // Split the query.
      QueryPair query = DataSourceHelper.splitQuery(dsRequest.getQuery(), Capabilities.SELECT);

      // Generate the data table.
      DataTable data = generateMyDataTable(query.getDataSourceQuery(), req);

      // Apply the completion query to the data table.
      DataTable newData = DataSourceHelper.applyQuery(query.getCompletionQuery(), data,
          dsRequest.getUserLocale());

      DataSourceHelper.setServletResponse(newData, dsRequest, resp);
    } catch (RuntimeException rte) {
      log.error("A runtime exception has occured", rte);
      ResponseStatus status = new ResponseStatus(StatusType.ERROR, ReasonType.INTERNAL_ERROR,
          rte.getMessage());
      if (dsRequest == null) {
        dsRequest = DataSourceRequest.getDefaultDataSourceRequest(req);
      }
      DataSourceHelper.setServletErrorResponse(status, dsRequest, resp);
    } catch (DataSourceException e) {
      if (dsRequest != null) {
        DataSourceHelper.setServletErrorResponse(e, dsRequest, resp);
      } else {
        DataSourceHelper.setServletErrorResponse(e, req, resp);
      }
    }
  }

  /**
   * Returns true if the given column name is requested in the given query.
   * If the query is empty, all columnNames returns true.
   *
   * @param query The given query.
   * @param columnName The requested column name.
   *
   * @return True if the given column name is requested in the given query.
   */
  private boolean isColumnRequested(Query query, String columnName) {
    // If the query is empty return true.
    if (query.isEmpty()) {
      return true;
    }

    List<AbstractColumn> columns = query.getSelection().getColumns();
    for (AbstractColumn column : columns) {
      if (column.getId().equalsIgnoreCase(columnName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Generates a data table - according to the provided tableId url parameter.
   *
   * @param query The query to operate on the underlying data.
   * @param req The HttpServeltRequest.
   *
   * @return The generated data table.
   */
  private DataTable generateMyDataTable(Query query, HttpServletRequest req)
		  throws TypeMismatchException {
	  String dataID = req.getParameter("dataId");
	  if (dataID != null) 
	  {
		  if (dataID.equalsIgnoreCase("AggressiveDrivers")) {
			  return getAggressiveDriversDataTable(query);
		  }else if (dataID.equalsIgnoreCase("FuelEfficientDrivers")) {
			  return getFuelEfficientDriversDataTable(query);
		  }else if (dataID.equalsIgnoreCase("CarsByCityModel")) {
			  return getCarsByCityModelDataTable(query);
		  }else if (dataID.equalsIgnoreCase("AverageSpeedByCity")) {
			  return getAverageSpeedByCityDataTable(query);
		  }else if (dataID.equalsIgnoreCase("AverageSpeedByModel")) {
			  return getAverageSpeedByModelDataTable(query);
		  }else if (dataID.equalsIgnoreCase("AllModelsAverageSpeedByCity")) {
			  return getAllModelsAverageSpeedByCityDataTable(query);
		  }
		  
		  
	  }

	  return null;//generateAnimalsTable(query);
  }

  /**
   * Returns an animals data table. The table contains a list of animals and
   * links to wikipedia pages.
   *
   * @param query The selection query.
   *
   * @return A data table of animals.
   *//*
  private DataTable generateAnimalsTable(Query query) throws TypeMismatchException {
    DataTable data = new DataTable();
    List<ColumnDescription> requiredColumns = getRequiredColumns(query,
        ANIMAL_TABLE_COLUMNS);
    data.addColumns(requiredColumns);

    // Populate the data table
    for (String key : animalLinksByName.keySet()) {
      TableRow row = new TableRow();
      for (ColumnDescription selectionColumn : requiredColumns) {
        String columnName = selectionColumn.getId();
        if (columnName.equals(ANIMAL_COLUMN)) {
          row.addCell(key);
        } else if (columnName.equals(ARTICLE_COLUMN)) {
          row.addCell(animalLinksByName.get(key));
        }
      }
      data.addRow(row);
    }
    return data;
  }*/

  /**
   * Returns the planets data table.
   *
   * @param query The selection query.
   *
   * @return planets data table.
   */
  private DataTable generatePlanetsTable(Query query) throws TypeMismatchException {
    DataTable data = new DataTable();
    List<ColumnDescription> requiredColumns = getRequiredColumns(
        query, planetTableColumns);
    data.addColumns(requiredColumns);
    
    List<Planet> planets=new ArrayList<>();
    planets.add(new Planet("JUPITER","Jupi",317.8, 11.2, 63));
    planets.add(new Planet("SATURN","Satur",95.2, 9.4, 60));
    planets.add(new Planet("URANUS","Uran",14.5, 4, 27));
    planets.add(new Planet("NEPTUNE","Nept",17.2, 3.9, 13));
    
    // Populate data table
    for (Planet planet : planets) {
      TableRow row = new TableRow();
      for (ColumnDescription selectionColumn : requiredColumns) {
        String columnName = selectionColumn.getId();
        if (columnName.equals(PLANET_COLUMN)) {
          row.addCell(planet.getName());
        } else if (columnName.equals(MODEL_COLUMN)) {
            row.addCell(planet.getModel());
        }else if (columnName.equals(MASS_COLUMN)) {
          row.addCell(planet.getMass());
        } else if (columnName.equals(GRAVITY_COLUMN)) {
          row.addCell(planet.getSurfaceGravity());
        } else if (columnName.equals(MOONS_COLUMN)) {
          row.addCell(planet.getNumberOfMoons());
        }
      }
      data.addRow(row);
    }
    return data;
  }
  
  
  /**
   * Returns the agrressive drivers data table.
   *
   * @param query The selection query.
   *
   * @return data table.
   */
  private DataTable getAggressiveDriversDataTable(Query query) throws TypeMismatchException {
    DataTable data = new DataTable();

    List<CarData> aggDriversList=new ArrayList<>();
    try {
		aggDriversList=new CarDataDao().getAggressiveDrivers();
	} catch (SQLException e) {
		System.out.println("Error on connecting to database:"+e.getMessage());
		e.printStackTrace();
	}
            
   /* var data = new google.visualization.DataTable();
    data.addColumn('string', 'Genre2');
    data.addColumn('number', 'Fantasy & Sci Fi2');
	data.addColumn('number', 'Romance2');
	
    data.addRows([
      ['2010', 10, 24],
    ['2020', 16, 22],
    ['2030', 28, 19]
    ]);*/
    // Populate data table
    
    //Map<String,List<Map<String,String>>> cityModelsMap=getDataMapByProperties("city","model",aggDrivers2);
    Map<String,List<Map<String,String>>> cityModelsMap=new HashMap<>();
    for(CarData carEvent1 : aggDriversList)
    {
    	Map<String,String> modelMap=new HashMap<>();
    	List<Map<String,String>> modelCountsList=new ArrayList<>();
    	if(!cityModelsMap.containsKey(carEvent1.getCity()))
    	{
    		modelMap.put(carEvent1.getModel(), carEvent1.getAggressiveDriversCount());
    		modelCountsList.add(modelMap);
    		cityModelsMap.put(carEvent1.getCity(),modelCountsList);
    	}else
    	{
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(carEvent1.getCity());
    		
    		modelMap.put(carEvent1.getModel(), carEvent1.getAggressiveDriversCount());
    		modelCountsList.add(modelMap);
    		existingModelCountsList.addAll(modelCountsList);
    		cityModelsMap.put(carEvent1.getCity(), existingModelCountsList);
    	}
    }
    
    //log.info("CityModelMap:"+cityModelsMap);
    data.addColumn(new ColumnDescription(CITY_COLUMN, ValueType.TEXT,CITY_COLUMN));
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
    	for(Map existingModelCountMap:existingModelCountsList)
    	{
    		Set set=existingModelCountMap.keySet();
    		for(Object objModel:set)
    		{
    			String model=objModel.toString();
    			if(!(data.containsColumn(model))){
    	        	data.addColumn(new ColumnDescription(model, ValueType.NUMBER,model));
    	        	//System.out.println("Column added:"+model);
    	        	}
    		}
    	}
    }
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	TableRow row = new TableRow();
    	
    	for (ColumnDescription selectionColumn : data.getColumnDescriptions()) {
    		String columnName = selectionColumn.getId();
    		if (columnName.equals(CITY_COLUMN)) {
    			row.addCell(cityKey);
    		}
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
	    	for(Map existingModelCountMap:existingModelCountsList)
	    	{
	    		for(Object objModel:existingModelCountMap.keySet())
	    		{
	    			String model=objModel.toString();
	    			Integer count=Integer.parseInt(existingModelCountMap.get(objModel).toString());
	    			if (columnName.equals(model)) {
		    			row.addCell(count);
		    		}
	    		}
	    	}
		}
    	data.addRow(row);
    }
    return data;
  }
  
  
  /**
   * Returns the fuel efficient drivers data table.
   *
   * @param query The selection query.
   *
   * @return data table.
   */
  private DataTable getFuelEfficientDriversDataTable(Query query) throws TypeMismatchException {
    DataTable data = new DataTable();

    List<CarData> aggDriversList=new ArrayList<>();
    try {
		aggDriversList=new CarDataDao().getFuelEffcntDrivers();
	} catch (SQLException e) {
		System.out.println("Error on connecting to database:"+e.getMessage());
		e.printStackTrace();
	}
    //Map<String,List<Map<String,String>>> cityModelsMap=getDataMapByProperties("city","model",aggDrivers2);
    Map<String,List<Map<String,String>>> cityModelsMap=new HashMap<>();
    for(CarData carEvent1 : aggDriversList)
    {
    	Map<String,String> modelMap=new HashMap<>();
    	List<Map<String,String>> modelCountsList=new ArrayList<>();
    	if(!cityModelsMap.containsKey(carEvent1.getCity()))
    	{
    		modelMap.put(carEvent1.getModel(), carEvent1.getFuelEffDriversCount());
    		modelCountsList.add(modelMap);
    		cityModelsMap.put(carEvent1.getCity(),modelCountsList);
    	}else
    	{
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(carEvent1.getCity());
    		
    		modelMap.put(carEvent1.getModel(), carEvent1.getFuelEffDriversCount());
    		modelCountsList.add(modelMap);
    		existingModelCountsList.addAll(modelCountsList);
    		cityModelsMap.put(carEvent1.getCity(), existingModelCountsList);
    	}
    }
    
    //log.info("CityModelMap:"+cityModelsMap);
    
    data.addColumn(new ColumnDescription(CITY_COLUMN, ValueType.TEXT,CITY_COLUMN));
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
    	for(Map existingModelCountMap:existingModelCountsList)
    	{
    		Set set=existingModelCountMap.keySet();
    		for(Object objModel:set)
    		{
    			String model=objModel.toString();
    			if(!(data.containsColumn(model))){
    	        	data.addColumn(new ColumnDescription(model, ValueType.NUMBER,model));
    	        	//System.out.println("Column added:"+model);
    	        	}
    		}
    	}
    }
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	TableRow row = new TableRow();
    	
    	for (ColumnDescription selectionColumn : data.getColumnDescriptions()) {
    		String columnName = selectionColumn.getId();
    		if (columnName.equals(CITY_COLUMN)) {
    			row.addCell(cityKey);
    		}
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
	    	for(Map existingModelCountMap:existingModelCountsList)
	    	{
	    		for(Object objModel:existingModelCountMap.keySet())
	    		{
	    			String model=objModel.toString();
	    			Integer count=Integer.parseInt(existingModelCountMap.get(objModel).toString());
	    			if (columnName.equals(model)) {
		    			row.addCell(count);
		    		}
	    		}
	    	}
		}
    	data.addRow(row);
    }
    return data;
  }
  

  
  /**
   * Returns the all the car data table by city and model.
   *
   * @param query The selection query.
   *
   * @return data table.
   */
  private DataTable getCarsByCityModelDataTable(Query query) throws TypeMismatchException {
	    DataTable data = new DataTable();

	    List<CarData> aggDriversList=new ArrayList<>();
	    try {
			aggDriversList=new CarDataDao().getCarsByCityModel();
		} catch (SQLException e) {
			System.out.println("Error on connecting to database:"+e.getMessage());
			e.printStackTrace();
		}
	    //Map<String,List<Map<String,String>>> cityModelsMap=getDataMapByProperties("city","model",aggDrivers2);
	    Map<String,List<Map<String,String>>> cityModelsMap=new HashMap<>();
	    for(CarData carEvent1 : aggDriversList)
	    {
	    	Map<String,String> modelMap=new HashMap<>();
	    	List<Map<String,String>> modelCountsList=new ArrayList<>();
	    	if(!cityModelsMap.containsKey(carEvent1.getCity()))
	    	{
	    		modelMap.put(carEvent1.getModel(), carEvent1.getCarCount());
	    		modelCountsList.add(modelMap);
	    		cityModelsMap.put(carEvent1.getCity(),modelCountsList);
	    	}else
	    	{
	    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(carEvent1.getCity());
	    		
	    		modelMap.put(carEvent1.getModel(), carEvent1.getCarCount());
	    		modelCountsList.add(modelMap);
	    		existingModelCountsList.addAll(modelCountsList);
	    		cityModelsMap.put(carEvent1.getCity(), existingModelCountsList);
	    	}
	    }
	    
	    //log.info("CityModelMap:"+cityModelsMap);
	    
	    data.addColumn(new ColumnDescription(CITY_COLUMN, ValueType.TEXT,CITY_COLUMN));
	    
	    for(String cityKey:cityModelsMap.keySet())
	    {
	    	List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
	    	for(Map existingModelCountMap:existingModelCountsList)
	    	{
	    		Set set=existingModelCountMap.keySet();
	    		for(Object objModel:set)
	    		{
	    			String model=objModel.toString();
	    			if(!(data.containsColumn(model))){
	    	        	data.addColumn(new ColumnDescription(model, ValueType.NUMBER,model));
	    	        	//System.out.println("Column added:"+model);
	    	        	}
	    		}
	    	}
	    }
	    
	    for(String cityKey:cityModelsMap.keySet())
	    {
	    	TableRow row = new TableRow();
	    	
	    	for (ColumnDescription selectionColumn : data.getColumnDescriptions()) {
	    		String columnName = selectionColumn.getId();
	    		if (columnName.equals(CITY_COLUMN)) {
	    			row.addCell(cityKey);
	    		}
	    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
		    	for(Map existingModelCountMap:existingModelCountsList)
		    	{
		    		for(Object objModel:existingModelCountMap.keySet())
		    		{
		    			String model=objModel.toString();
		    			Integer count=Integer.parseInt(existingModelCountMap.get(objModel).toString());
		    			if (columnName.equals(model)) {
			    			row.addCell(count);
			    		}
		    		}
		    	}
			}
	    	data.addRow(row);
	    }
	    return data;
	 }
  
 
  /**
   * Returns the average data table of speed,engine temparature and engine oil by city.
   *
   * @param query The selection query.
   *
   * @return data table.
   */
  private DataTable getAverageSpeedByCityDataTable(Query query) throws TypeMismatchException {
    DataTable data = new DataTable();

    List<CarData> aggDriversList=new ArrayList<>();
    try {
		aggDriversList=new CarDataDao().getAverageSpeedByCity();
	} catch (SQLException e) {
		System.out.println("Error on connecting to database:"+e.getMessage());
		e.printStackTrace();
	}
    
    /* var data = new google.visualization.DataTable();
    data.addColumn('string', 'Genre2');
    data.addColumn('number', 'Fantasy & Sci Fi2');
	data.addColumn('number', 'Romance2');
	
    data.addRows([
      ['2010', 10, 24],
    ['2020', 16, 22],
    ['2030', 28, 19]
    ]);*/
    
    
    //Map<String,List<Map<String,String>>> cityModelsMap=getDataMapByProperties("city","model",aggDrivers2);
    Map<String,List<Map<String,String>>> cityModelsMap=new HashMap<>();
    for(CarData carEvent1 : aggDriversList)
    {
    	Map<String,String> modelMap=new HashMap<>();
    	List<Map<String,String>> modelCountsList=new ArrayList<>();
    	if(!cityModelsMap.containsKey(carEvent1.getCity()))
    	{
    		modelMap.put("AverageSpeed", carEvent1.getAverageSpeed());
    		modelMap.put("AverageEngTemp", carEvent1.getAverageEngTemp());
    		modelMap.put("AverageEngOil", carEvent1.getAverageEngOil());
    		
    		modelCountsList.add(modelMap);
    		cityModelsMap.put(carEvent1.getCity(),modelCountsList);
    	}else
    	{
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(carEvent1.getCity());
    		
    		modelMap.put("AverageSpeed", carEvent1.getAverageSpeed());
    		modelMap.put("AverageEngTemp", carEvent1.getAverageEngTemp());
    		modelMap.put("AverageEngOil", carEvent1.getAverageEngOil());
    		
    		modelCountsList.add(modelMap);
    		existingModelCountsList.addAll(modelCountsList);
    		cityModelsMap.put(carEvent1.getCity(), existingModelCountsList);
    	}
    }
    
    //log.info("CityModelMap:"+cityModelsMap);
    
    data.addColumn(new ColumnDescription(CITY_COLUMN, ValueType.TEXT,CITY_COLUMN));
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
    	for(Map existingModelCountMap:existingModelCountsList)
    	{
    		Set set=existingModelCountMap.keySet();
    		for(Object objModel:set)
    		{
    			String model=objModel.toString();
    			if(!(data.containsColumn(model))){
    	        	data.addColumn(new ColumnDescription(model, ValueType.NUMBER,model));
    	        	//System.out.println("Column added:"+model);
    	        	}
    		}
    	}
    }
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	TableRow row = new TableRow();
    	
    	for (ColumnDescription selectionColumn : data.getColumnDescriptions()) {
    		String columnName = selectionColumn.getId();
    		if (columnName.equals(CITY_COLUMN)) {
    			row.addCell(cityKey);
    		}
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
	    	for(Map existingModelCountMap:existingModelCountsList)
	    	{
	    		for(Object objModel:existingModelCountMap.keySet())
	    		{
	    			String model=objModel.toString();
	    			Double count=Double.valueOf(existingModelCountMap.get(objModel).toString());
	    			if (columnName.equals(model)) {
		    			row.addCell(count);
		    		}
	    		}
	    	}
		}
    	data.addRow(row);
    }
    return data;
  }
  
  /**
   * Returns the average data table of speed,engine temparature and engine oil by city.
   *
   * @param query The selection query.
   *
   * @return data table.
   */
  private DataTable getAverageSpeedByModelDataTable(Query query) throws TypeMismatchException {
    DataTable data = new DataTable();

    List<CarData> aggDriversList=new ArrayList<>();
    try {
		aggDriversList=new CarDataDao().getAverageSpeedByModel();
	} catch (SQLException e) {
		System.out.println("Error on connecting to database:"+e.getMessage());
		e.printStackTrace();
	}
    
    /* var data = new google.visualization.DataTable();
    data.addColumn('string', 'Genre2');
    data.addColumn('number', 'Fantasy & Sci Fi2');
	data.addColumn('number', 'Romance2');
	
    data.addRows([
      ['2010', 10, 24],
    ['2020', 16, 22],
    ['2030', 28, 19]
    ]);*/
    
    
    //Map<String,List<Map<String,String>>> cityModelsMap=getDataMapByProperties("city","model",aggDrivers2);
    Map<String,List<Map<String,String>>> cityModelsMap=new HashMap<>();
    for(CarData carEvent1 : aggDriversList)
    {
    	Map<String,String> modelMap=new HashMap<>();
    	List<Map<String,String>> modelCountsList=new ArrayList<>();
    	if(!cityModelsMap.containsKey(carEvent1.getModel()))
    	{
    		modelMap.put("AverageSpeed", carEvent1.getAverageSpeed());
    		modelMap.put("AverageEngTemp", carEvent1.getAverageEngTemp());
    		modelMap.put("AverageEngOil", carEvent1.getAverageEngOil());
    		
    		modelCountsList.add(modelMap);
    		cityModelsMap.put(carEvent1.getModel(),modelCountsList);
    	}else
    	{
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(carEvent1.getModel());
    		
    		modelMap.put("AverageSpeed", carEvent1.getAverageSpeed());
    		modelMap.put("AverageEngTemp", carEvent1.getAverageEngTemp());
    		modelMap.put("AverageEngOil", carEvent1.getAverageEngOil());
    		
    		modelCountsList.add(modelMap);
    		existingModelCountsList.addAll(modelCountsList);
    		cityModelsMap.put(carEvent1.getModel(), existingModelCountsList);
    	}
    }
    
    //log.info("CityModelMap:"+cityModelsMap);
    
    data.addColumn(new ColumnDescription(MODEL_COLUMN, ValueType.TEXT,MODEL_COLUMN));
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
    	for(Map existingModelCountMap:existingModelCountsList)
    	{
    		Set set=existingModelCountMap.keySet();
    		for(Object objModel:set)
    		{
    			String model=objModel.toString();
    			if(!(data.containsColumn(model))){
    	        	data.addColumn(new ColumnDescription(model, ValueType.NUMBER,model));
    	        	//System.out.println("Column added:"+model);
    	        	}
    		}
    	}
    }
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	TableRow row = new TableRow();
    	
    	for (ColumnDescription selectionColumn : data.getColumnDescriptions()) {
    		String columnName = selectionColumn.getId();
    		if (columnName.equals(MODEL_COLUMN)) {
    			row.addCell(cityKey);
    		}
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
	    	for(Map existingModelCountMap:existingModelCountsList)
	    	{
	    		for(Object objModel:existingModelCountMap.keySet())
	    		{
	    			String model=objModel.toString();
	    			Double count=Double.valueOf(existingModelCountMap.get(objModel).toString());
	    			if (columnName.equals(model)) {
		    			row.addCell(count);
		    		}
	    		}
	    	}
		}
    	data.addRow(row);
    }
    return data;
  }

  
  /**
   * Returns the average data table of speed,engine temparature and engine oil by city.
   *
   * @param query The selection query.
   *
   * @return data table.
   */
  private DataTable getAllModelsAverageSpeedByCityDataTable(Query query) throws TypeMismatchException {
    DataTable data = new DataTable();

    List<CarData> carDataList=new ArrayList<>();
    try {
		carDataList=new CarDataDao().getAllModelAverageSpeedByCity();
	} catch (SQLException e) {
		System.out.println("Error on connecting to database:"+e.getMessage());
		e.printStackTrace();
	}
    
    /* var data = google.visualization.arrayToDataTable([
        ['City',   'Population', 'Area'],
        ['Rome',      2761477,    1285.31],
        ['Milan',     1324110,    181.76],
        ['Naples',    959574,     117.27],
        ['Turin',     907563,     130.17],
        ['Palermo',   655875,     158.9],
        ['Genoa',     607906,     243.60],
        ['Bologna',   380181,     140.7],
        ['Florence',  371282,     102.41],
        ['Fiumicino', 67370,      213.44],
        ['Anzio',     52192,      43.43],
        ['Ciampino',  38262,      11]
      ]);*/
    
    
    //Map<String,List<Map<String,String>>> cityModelsMap=getDataMapByProperties("city","model",aggDrivers2);
    Map<String,List<Map<String,String>>> cityModelsMap=new HashMap<>();
    for(CarData carEvent1 : carDataList)
    {
    	Map<String,String> modelMap=new HashMap<>();
    	List<Map<String,String>> modelCountsList=new ArrayList<>();
    	if(!cityModelsMap.containsKey(carEvent1.getCity()))
    	{
    		modelMap.put("AllModelsCount", carEvent1.getCount());
    		modelMap.put("AverageSpeed", carEvent1.getAverageSpeed());
    		modelCountsList.add(modelMap);
    		cityModelsMap.put(carEvent1.getCity(),modelCountsList);
    	}else
    	{
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(carEvent1.getCity());
    		
    		modelMap.put("AllModelsCount", carEvent1.getCount());
    		modelMap.put("AverageSpeed", carEvent1.getAverageSpeed());
    		
    		modelCountsList.add(modelMap);
    		existingModelCountsList.addAll(modelCountsList);
    		cityModelsMap.put(carEvent1.getCity(), existingModelCountsList);
    	}
    }
    
    //log.info("CityModelMap:"+cityModelsMap);
    
    data.addColumn(new ColumnDescription("City", ValueType.TEXT, "City"));
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
    	for(Map existingModelCountMap:existingModelCountsList)
    	{
    		Set set=existingModelCountMap.keySet();
    		for(Object objModel:set)
    		{
    			String model=objModel.toString();
    			if(!(data.containsColumn(model))){
    	        	data.addColumn(new ColumnDescription(model, ValueType.NUMBER,model));
    	        	//System.out.println("Column added:"+model);
    	        	}
    		}
    	}
    }
    
    for(String cityKey:cityModelsMap.keySet())
    {
    	TableRow row = new TableRow();
    	
    	for (ColumnDescription selectionColumn : data.getColumnDescriptions()) {
    		String columnName = selectionColumn.getId();
    		if (columnName.equals("City")) {
    			row.addCell(cityKey);
    		}
    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
	    	for(Map existingModelCountMap:existingModelCountsList)
	    	{
	    		for(Object objModel:existingModelCountMap.keySet())
	    		{
	    			String model=objModel.toString();
	    			Double count=Double.valueOf(existingModelCountMap.get(objModel).toString());
	    			if (columnName.equals(model)) {
		    			row.addCell(count);
		    		}
	    		}
	    	}
		}
    	data.addRow(row);
    }
    return data;
  }
/**
   * Returns a list of required columns based on the query and the actual
   * columns.
   *
   * @param query The user selection query.
   * @param availableColumns The list of possible columns.
   *
   * @return A List of required columns for the requested data table.
   */
  private List<ColumnDescription> getRequiredColumns(Query query,
      ColumnDescription[] availableColumns) {
    // Required columns
    List<ColumnDescription> requiredColumns = Lists.newArrayList();
    for (ColumnDescription column : availableColumns) {
      if (isColumnRequested(query, column.getId())) {
        requiredColumns.add(column);
      }
    }
    return requiredColumns;
  }
  
  /**Get the Data map by given properties of the list
 * @param string
 * @param string2
 * @return
 */
private Map<String, List<Map<String, String>>> getDataMapByProperties(String prop1, String prop2,List<CarData> list) {
		
	Map<String,List<Map<String,String>>> dataMap=new HashMap<>();
    for(CarData carEvent1 : list)
    {
    	Map<String,String> modelMap=new HashMap<>();
    	List<Map<String,String>> modelCountsList=new ArrayList<>();
    	if(!dataMap.containsKey(carEvent1.getCity()))
    	{
    		modelMap.put(carEvent1.getModel(), carEvent1.getAggressiveDriversCount());
    		modelCountsList.add(modelMap);
    		dataMap.put(carEvent1.getCity(),modelCountsList);
    	}else
    	{
    		List<Map<String,String>> existingModelCountsList=dataMap.get(carEvent1.getCity());
    		
    		modelMap.put(carEvent1.getModel(), carEvent1.getAggressiveDriversCount());
    		modelCountsList.add(modelMap);
    		existingModelCountsList.addAll(modelCountsList);
    		dataMap.put(carEvent1.getCity(), existingModelCountsList);
    	}
    }
		return dataMap;
		/*Map<String,List<Map<String,String>>> cityModelsMap=new HashMap<>();
	    for(CarData carEvent1 : aggDrivers2)
	    {
	    	Map<String,String> modelMap=new HashMap<>();
	    	List<Map<String,String>> modelCountsList=new ArrayList<>();
	    	if(!cityModelsMap.containsKey(carEvent1.getCity()))
	    	{
	    		modelMap.put(carEvent1.getModel(), carEvent1.getCount());
	    		modelCountsList.add(modelMap);
	    		cityModelsMap.put(carEvent1.getCity(),modelCountsList);
	    	}else
	    	{
	    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(carEvent1.getCity());
	    		
	    		modelMap.put(carEvent1.getModel(), carEvent1.getCount());
	    		modelCountsList.add(modelMap);
	    		existingModelCountsList.addAll(modelCountsList);
	    		cityModelsMap.put(carEvent1.getCity(), existingModelCountsList);
	    	}
	    }*/
	}
  
  
  
  /**To fetch Mock data for testing
 * @return
 */
private List<CarEvent> getAggressiveDriversMock() {
	  List<CarEvent> aggDrivers=new ArrayList<>();

	  /*CityModelMap:{Seattle=[{Medium SUV=1038}, {Family Saloon=2388}, {Large SUV=2415}, {Sports Car=1626}, {sports car=276}, {Compact car=204}, {Small SUV=1133}, {Station Wagon=1769}, {Compact Car=839}, {Hybrid=2603}, {Coupe=1081}, {Sedan=2603}, {Convertible=1608}], 
		Redmond=[{Medium SUV=590}, {Family Saloon=1535}, {Large SUV=1407}, {Sports Car=1115}, {sports car=102}, {Compact car=102}, {Small SUV=637}, {Station Wagon=1079}, {Compact Car=606}, {Coupe=605}, {Sedan=1568}, {Hybrid=1635}, {Convertible=955}], 
		Sammamish=[{Sports Car=1}], 
		Bellevue=[{Convertible=1122}, {Medium SUV=778}, {Family Saloon=1952}, {Large SUV=2035}, {Sports Car=1226}, {Compact car=162}, {sports car=192}, {Small SUV=895}, {Station Wagon=1469}, {Compact Car=629}, {Hybrid=1989}, {Coupe=811}, {Sedan=2004}]}
*/

/*List<CarEvent> aggDrivers=new ArrayList<>();
for(CarData carData:aggDrivers2)
{
	aggDrivers.add(new CarEvent(carData.getCity(),carData.getModel(),carData.getCount()));
}*/

	  
	    /*aggDrivers.add(new CarEvent("Seattle","Medium SUV",2388));
	    aggDrivers.add(new CarEvent("Seattle","Family Saloon",2415));
	    aggDrivers.add(new CarEvent("Seattle","Large SUV",1626));
	    
	    aggDrivers.add(new CarEvent("Seattle","Sports Car",1241));
	    aggDrivers.add(new CarEvent("Seattle","sports car",1734));
	    aggDrivers.add(new CarEvent("Seattle","Compact car",3622));
	    
	    aggDrivers.add(new CarEvent("Seattle","Hybrid",4112));
	    aggDrivers.add(new CarEvent("Seattle","Sedan",1712));
	    aggDrivers.add(new CarEvent("Seattle","Convertible",3611));
	    
	    aggDrivers.add(new CarEvent("Seattle","model19",4123));
	    aggDrivers.add(new CarEvent("Seattle","Convertible",173));
	    aggDrivers.add(new CarEvent("Seattle","model111",364));
	    
	    aggDrivers.add(new CarEvent("city1","model112",4144));
	    aggDrivers.add(new CarEvent("city1","Family Saloon",1755));
	    aggDrivers.add(new CarEvent("city1","Convertible",3666));
	    
	    aggDrivers.add(new CarEvent("city1","model16",41));
	    aggDrivers.add(new CarEvent("city1","model17",17));
	    aggDrivers.add(new CarEvent("city1","model18",36));
	    
	    aggDrivers.add(new CarEvent("city1","model19",41));
	    aggDrivers.add(new CarEvent("city1","Sports Car",17));
	    aggDrivers.add(new CarEvent("city1","model111",36));
	    
	    aggDrivers.add(new CarEvent("city1","model112",41));
	    aggDrivers.add(new CarEvent("city1","model113",17));
	    aggDrivers.add(new CarEvent("city1","model114",36));
	    
	    aggDrivers.add(new CarEvent("city4","model",1));
	    
	    aggDrivers.add(new CarEvent("Redmond","Convertible",31));
	    aggDrivers.add(new CarEvent("Redmond","model21",30));
	    aggDrivers.add(new CarEvent("Redmond","Sports Car",37));
	    
	    
	    aggDrivers.add(new CarEvent("Redmond","model23",3134));
	    aggDrivers.add(new CarEvent("Redmond","model24",3033));
	    aggDrivers.add(new CarEvent("Redmond","Sports Car",337));
	    
	    
	    aggDrivers.add(new CarEvent("Redmond","model26",3144));
	    aggDrivers.add(new CarEvent("Redmond","Convertible",3120));
	    aggDrivers.add(new CarEvent("Redmond","model28",4337));
	    
	    
	    aggDrivers.add(new CarEvent("Redmond","model29",3100));
	    aggDrivers.add(new CarEvent("Redmond","Family Saloon",30));
	    aggDrivers.add(new CarEvent("Redmond","model211",37));
	    
	    
	    aggDrivers.add(new CarEvent("Redmond","model212",31));
	    aggDrivers.add(new CarEvent("Redmond","model213",303));
	    aggDrivers.add(new CarEvent("Redmond","Convertible",37));
	    
	    
	    aggDrivers.add(new CarEvent("Bellevue","Sports Car",4711));
	    aggDrivers.add(new CarEvent("Bellevue","model31",4122));
	    aggDrivers.add(new CarEvent("Bellevue","Convertible",36));*/
	    
	    /*aggDrivers.add(new CarEvent("Seattle","Medium SUV",1038)); 
	    aggDrivers.add(new CarEvent("Seattle","Family Saloon",2388)); 
	    aggDrivers.add(new CarEvent("Seattle","Large SUV",2415)); 
	    aggDrivers.add(new CarEvent("Seattle","Sports Car",1626)); 
	    //aggDrivers.add(new CarEvent("Seattle","sports car",276)); 
	    aggDrivers.add(new CarEvent("Seattle","Compact car",204)); 
	    aggDrivers.add(new CarEvent("Seattle","Small SUV",1133)); 
	    aggDrivers.add(new CarEvent("Seattle","Station Wagon",1769)); 
	    aggDrivers.add(new CarEvent("Seattle","Compact Car",839)); 
	    aggDrivers.add(new CarEvent("Seattle","Hybrid",2603)); 
	    aggDrivers.add(new CarEvent("Seattle","Coupe",1081)); 
	    aggDrivers.add(new CarEvent("Seattle","Sedan",2603));
	    aggDrivers.add(new CarEvent("Seattle","Convertible",1608));

	    aggDrivers.add(new CarEvent("Redmond","Medium SUV",590)); 
	    aggDrivers.add(new CarEvent("Redmond","Family Saloon",1535)); 
	    aggDrivers.add(new CarEvent("Redmond","Large SUV",1407)); 
	    aggDrivers.add(new CarEvent("Redmond","Sports Car",1115)); 
	    //aggDrivers.add(new CarEvent("Redmond","sports car",102)); 
	    aggDrivers.add(new CarEvent("Redmond","Compact car",102)); 
	    aggDrivers.add(new CarEvent("Redmond","Small SUV",637)); 
	    aggDrivers.add(new CarEvent("Redmond","Station Wagon",1079));
	    aggDrivers.add(new CarEvent("Redmond","Compact Car",606));
	    aggDrivers.add(new CarEvent("Redmond","Coupe",605)); 
	    aggDrivers.add(new CarEvent("Redmond","Sedan",1568)); 
	    aggDrivers.add(new CarEvent("Redmond","Hybrid",1635)); 
	    aggDrivers.add(new CarEvent("Redmond","Convertible",955)); 


	    aggDrivers.add(new CarEvent("ammamish","Sports Car",100)); 
	    aggDrivers.add(new CarEvent("ammamish","Sedan",200)); 

	    aggDrivers.add(new CarEvent("ellevue","Convertible",1122)); 
	    aggDrivers.add(new CarEvent("ellevue","Medium SUV",778));
	    aggDrivers.add(new CarEvent("ellevue","Family Saloon",1952)); 
	    aggDrivers.add(new CarEvent("ellevue","Large SUV",2035)); 
	    aggDrivers.add(new CarEvent("ellevue","Sports Car",1226));
	    aggDrivers.add(new CarEvent("ellevue","Compact car",162));
	    //aggDrivers.add(new CarEvent("ellevue","sports car",192)); 
	    aggDrivers.add(new CarEvent("ellevue","Small SUV",895));
	    aggDrivers.add(new CarEvent("ellevue","Station Wagon",1469));
	    aggDrivers.add(new CarEvent("ellevue","Compact Car",629));
	    aggDrivers.add(new CarEvent("ellevue","Hybrid",1989)); 
	    aggDrivers.add(new CarEvent("ellevue","Coupe",811)); 
	    aggDrivers.add(new CarEvent("ellevue","Sedan",2004));*/
	    
		return aggDrivers;
	}
  
  public static void main(String[] args) throws TypeMismatchException {
	
	   List<CarEvent> carData=new LinkedList<>();
	    /*carData.add(new CarEvent("city1","model1",41));
	    carData.add(new CarEvent("city1","model2",17));
	    
	    carData.add(new CarEvent("city2","model1",31));
	    carData.add(new CarEvent("city2","model2",39));
	    
	    carData.add(new CarEvent("Bellevue","model1",47));*/
	   
	   
	    /*carData.add(new CarEvent("Seattle","MediumSUV",1038)); 
	    carData.add(new CarEvent("Seattle","LargeSUV",2415));
	    carData.add(new CarEvent("Seattle","FamilySaloon",2388));
	    carData.add(new CarEvent("Seattle","SportsCar",1626)); 
	    //aggDrivers.add(new CarEvent("Seattle","sports car",276)); 
	    carData.add(new CarEvent("Seattle","Compactcar",204)); 
	    carData.add(new CarEvent("Seattle","SmallSUV",1133)); 
	    carData.add(new CarEvent("Seattle","StationWagon",1769)); 
	    carData.add(new CarEvent("Seattle","CompactCar",839)); 
	    carData.add(new CarEvent("Seattle","Hybrid",2603)); 
	    carData.add(new CarEvent("Seattle","Coupe",1081)); 
	    carData.add(new CarEvent("Seattle","Sedan",2603));
	    carData.add(new CarEvent("Seattle","Convertible",1608));

	    carData.add(new CarEvent("Redmond","MediumSUV",590));
	    carData.add(new CarEvent("Redmond","LargeSUV",1407)); 
	    carData.add(new CarEvent("Redmond","FamilySaloon",1535)); 
	    carData.add(new CarEvent("Redmond","SportsCar",1115)); 
	    
	    //aggDrivers.add(new CarEvent("Redmond","sports car",102)); 
	    carData.add(new CarEvent("Redmond","Compactcar",102)); 
	    carData.add(new CarEvent("Redmond","SmallSUV",637)); 
	    carData.add(new CarEvent("Redmond","StationWagon",1079));
	    carData.add(new CarEvent("Redmond","CompactCar",606));
	    carData.add(new CarEvent("Redmond","Hybrid",1635));
	    carData.add(new CarEvent("Redmond","Coupe",605)); 
	    carData.add(new CarEvent("Redmond","Sedan",1568)); 
	    carData.add(new CarEvent("Redmond","Convertible",955)); 

	    carData.add(new CarEvent("ammamish","SportsCar",1)); 
	    carData.add(new CarEvent("ammamish","Sedan",21)); 
	    
	    carData.add(new CarEvent("ellevue","MediumSUV",778));
	    carData.add(new CarEvent("ellevue","LargeSUV",2035));
	    carData.add(new CarEvent("ellevue","FamilySaloon",1952)); 
	    carData.add(new CarEvent("ellevue","SportsCar",1226));
	    carData.add(new CarEvent("ellevue","Compactcar",162));
	    //aggDrivers.add(new CarEvent("ellevue","sports car",192)); 
	    carData.add(new CarEvent("ellevue","SmallSUV",895));
	    carData.add(new CarEvent("ellevue","StationWagon",1469));
	    carData.add(new CarEvent("ellevue","CompactCar",629));
	    carData.add(new CarEvent("ellevue","Hybrid",1989)); 
	    carData.add(new CarEvent("ellevue","Coupe",811)); 
	    carData.add(new CarEvent("ellevue","Sedan",2004));
	    carData.add(new CarEvent("ellevue","Convertible",1122));*/
	    
	    
	    Map<String,List<Map<String,String>>> cityModelsMap=new LinkedHashMap<>();
	    for(CarEvent carEvent1 : carData)
	    {
	    	Map<String,String> modelMap=new HashMap<>();
	    	List<Map<String,String>> modelCountsList=new ArrayList<>();
	    	if(!cityModelsMap.containsKey(carEvent1.getCity()))
	    	{
	    		modelMap.put(carEvent1.getModel(), carEvent1.getCount());
	    		modelCountsList.add(modelMap);
	    		cityModelsMap.put(carEvent1.getCity(),modelCountsList);
	    	}else
	    	{
	    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(carEvent1.getCity());
	    		
	    		modelMap.put(carEvent1.getModel(), carEvent1.getCount());
	    		modelCountsList.add(modelMap);
	    		existingModelCountsList.addAll(modelCountsList);
	    		cityModelsMap.put(carEvent1.getCity(), existingModelCountsList);
	    	}
	    }
	    System.out.println("CityModelMap:"+cityModelsMap);
	    //CityModelMap:{city1=[{model1=41}, {model11=17}, {model12=36}], city2=[{model2=31}, {model22=37}], city3=[{model3=47}, {model33=31}]}
	    DataTable data = new DataTable();
	    data.addColumn(new ColumnDescription(CITY_COLUMN, ValueType.TEXT,"city"));
	    for(String cityKey:cityModelsMap.keySet())
	    {
	    	List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
	    	for(Map existingModelCountMap:existingModelCountsList)
	    	{
	    		Set set=existingModelCountMap.keySet();
	    		for(Object objModel:set)
	    		{
	    			String model=objModel.toString();
	    			if(!(data.containsColumn(model))){
	    	        	data.addColumn(new ColumnDescription(model, ValueType.NUMBER,model));
	    	        	System.out.println("Column added:"+model);
	    	        	}
	    		}
	    	}
	    }
	    
	    for(String cityKey:cityModelsMap.keySet())
	    {
	    	TableRow row = new TableRow();
	    	for (ColumnDescription selectionColumn : data.getColumnDescriptions()) {
	    		String columnName = selectionColumn.getId();
	    		if (columnName.equals(CITY_COLUMN)) {
	    			row.addCell(cityKey);
	    			continue;
	    		}
	    		List<Map<String,String>> existingModelCountsList=cityModelsMap.get(cityKey);
	    		for(Map existingModelCountMap:existingModelCountsList)
		    	{
	    			for(Object objModel:existingModelCountMap.keySet())
		    		{
		    			String model=objModel.toString();
		    			Integer count=Integer.parseInt(existingModelCountMap.get(objModel).toString());
		    			System.out.println("Model :"+model+" Count:"+count);
		    			if (columnName.equals(model)) {
			    			row.addCell(count);
			    			continue;
			    		}
		    		}
		    	}
			}
	    	data.addRow(row);
	    	System.out.println("Adding row");
	    }
	    System.out.println("Data is now:"+data.toString());
	    
}
}
