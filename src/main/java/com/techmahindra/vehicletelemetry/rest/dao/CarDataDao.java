package com.techmahindra.vehicletelemetry.rest.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.techmahindra.vehicletelemetry.rest.bean.CarAlert;
import com.techmahindra.vehicletelemetry.rest.bean.CarData;

public class CarDataDao {
	
	
    /*private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";*/
	//private static final String DB_CONNECTION = "jdbc:h2:~/testdb";
	
	/*private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_CONNECTION = "jdbc:h2:file:~/h2/cardb";
	//private static final String DB_CONNECTION = "jdbc:h2:./test2";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";*/
    
    private static Connection getDBConnection() {
    	Properties props = new Properties();
			try {
				props.load(CarDataDao.class.getClassLoader().getResourceAsStream("vt.properties"));
			} catch (IOException e1) {
				System.out.println("Error loading properties..:"+e1.getMessage());
				e1.printStackTrace();
			}
		
        Connection dbConnection = null;
        try {
            Class.forName(props.getProperty("JDBC_DRIVER"));
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(props.getProperty("JDBC_CONN_STRING"), 
					props.getProperty("JDBC_USER"), 
					props.getProperty("JDBC_PASSWORD"));
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
    
    public static void main(String[] args) throws Exception {
        try {

        	/*new CarDataDao().getFuelEffcntDrivers();
        	new CarDataDao().getAggressiveDrivers();  
        	new CarDataDao().getCarAlerts();
        	new CarDataDao().getCarsByCityModel();
        	new CarDataDao().getAverageSpeedByModel();
        	new CarDataDao().getAverageSpeedByCity();*/
        	new CarDataDao().getAllModelAverageSpeedByCity();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<CarData> getCarsByCityModel() throws SQLException 
	{
		Connection connection = getDBConnection();
        Statement stmt = null;
        List<CarData> aggressiveData=new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT city,model, count(*)as count  FROM CAR group by city,model");
            //ResultSet rs = stmt.executeQuery("SELECT city,model, count(*)as count  FROM CAR WHERE brake_pedal_status = 1 AND speed >= 50 group by city,model");
            //System.out.println("------------------getCarsByCityModel--------------------");
            while (rs !=null && rs.next()) {
                
            	String city=rs.getString("city");
            	String model=rs.getString("model");
				String count=rs.getString("count");
            	//System.out.println("city :" +city +" model :" +model +" count :" +count );
                CarData car=new CarData();
				car.setModel(model);
				car.setCity(city);
				car.setCarCount(count);
                aggressiveData.add(car);
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        //System.out.println("Data :"+aggressiveData);
		return aggressiveData;
	}
    
   
	public List<CarData> getAggressiveDrivers() throws SQLException 
	{
		Connection connection = getDBConnection();
        Statement stmt = null;
        List<CarData> aggressiveData=new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT city,model, count(*)as count  FROM CAR WHERE transmission_gear_position IN ('fourth', 'fifth', 'sixth', 'seventh', 'eight') AND brake_pedal_status = 1 AND speed >= 50 group by city,model");
            //ResultSet rs = stmt.executeQuery("SELECT city,model, count(*)as count  FROM CAR WHERE brake_pedal_status = 1 AND speed >= 50 group by city,model");
            //System.out.println("------------------getAggressiveDrivers--------------------");
            while (rs !=null && rs.next()) {
                
            	String city=rs.getString("city");
            	String model=rs.getString("model");
				String count=rs.getString("count");
            	//System.out.println("city :" +city +" model :" +model +" count :" +count );
                CarData car=new CarData();
				car.setModel(model);
				car.setCity(city);
				car.setAggressiveDriversCount(count);
                aggressiveData.add(car);
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        //System.out.println("Data :"+aggressiveData);
		return aggressiveData;
	}
	
	public List<CarData> getFuelEffcntDrivers() throws SQLException 
	{
		Connection connection = getDBConnection();
        Statement stmt = null;
        List<CarData> fuelEffData=new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT model, city, count(*) as count FROM CAR WHERE transmission_gear_position IN ('fourth', 'fifth', 'sixth', 'seventh', 'eight') AND parking_brake_status = 0 AND brake_pedal_status = 0  AND speed <= 60 AND accelerator_pedal_position >= 50 group by city,model");
            //System.out.println("------------------getFuelEffcntDrivers--------------------");
            while (rs !=null && rs.next()) {
            	
            	String city=rs.getString("city");
            	String model=rs.getString("model");
				String count=rs.getString("count");
            	//System.out.println("model :" +model +" city :" +city +" count :" +count );
                CarData car=new CarData();
				car.setModel(model);
				car.setCity(city);
				car.setFuelEffDriversCount(count);
                fuelEffData.add(car);
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
		return fuelEffData;
	}
	
	
	public List<CarData> getAverageSpeedByCity() throws SQLException 
	{
		Connection connection = getDBConnection();
        Statement stmt = null;
        List<CarData> aggressiveData=new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT city,avg(speed) as avgspeed,avg(enginetemperature) as avgengtemp,avg(engineoil) as avgengoil FROM CAR group by city");//group by city,avgspeed,avgengtemp,avgengoil
            //System.out.println("------------------getAverageSpeedByCity--------------------");
            while (rs !=null && rs.next()) {
                //city,avgspeed,avgengtemp,avgengoil
            	String city=rs.getString("city");
            	String avgspeed=rs.getString("avgspeed");
            	String avgengtemp=rs.getString("avgengtemp");
            	String avgengoil=rs.getString("avgengoil");
            	
            	//System.out.println("city :" +city +" avgspeed :" +avgspeed +" avgengtemp :" +avgengtemp +" avgengoil :" +avgengoil );
                CarData car=new CarData();
				car.setCity(city);
				car.setAverageSpeed(avgspeed);
				car.setAverageEngTemp(avgengtemp);
				car.setAverageEngOil(avgengoil);
				aggressiveData.add(car);
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
		return aggressiveData;
	}
	
	
	public List<CarData> getAverageSpeedByModel() throws SQLException 
	{
		Connection connection = getDBConnection();
        Statement stmt = null;
        List<CarData> aggressiveData=new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT model,avg(speed) as avgspeed,avg(enginetemperature) as avgengtemp,avg(engineoil) as avgengoil FROM CAR group by model");
            //System.out.println("------------------getAverageSpeedByModel--------------------");
            while (rs !=null && rs.next()) {
                //city,avgspeed,avgengtemp,avgengoil
            	String model=rs.getString("model");
            	String avgspeed=rs.getString("avgspeed");
            	String avgengtemp=rs.getString("avgengtemp");
            	String avgengoil=rs.getString("avgengoil");
            	
            	//System.out.println("model :" +model +" avgspeed :" +avgspeed +" avgengtemp :" +avgengtemp +" avgengoil :" +avgengoil );
                CarData car=new CarData();
				car.setModel(model);
				car.setAverageSpeed(avgspeed);
				car.setAverageEngTemp(avgengtemp);
				car.setAverageEngOil(avgengoil);
				
				aggressiveData.add(car);
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
		return aggressiveData;
	}
	
	
	
	public List<CarData> getAllModelAverageSpeedByCity() throws SQLException 
	{
		Connection connection = getDBConnection();
        Statement stmt = null;
        List<CarData> aggressiveData=new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT city ,count(model) as modelcount,avg(speed) as avgspeed FROM CAR group by city");
            //System.out.println("------------------getAverageSpeedByModel--------------------");
            while (rs !=null && rs.next()) {
                //city,avgspeed,avgengtemp,avgengoil
            	String city=rs.getString("city");
            	String avgspeed=rs.getString("avgspeed");
            	String modelcount=rs.getString("modelcount");
            	//String avgengoil=rs.getString("avgengoil");
            	
            	System.out.println("city :" +city +" modelcount :" +modelcount +" avgspeed :" +avgspeed );
                CarData car=new CarData();
				car.setCity(city);
				car.setAverageSpeed(avgspeed);
				car.setCount(modelcount);
				//car.setAverageEngOil(avgengoil);
				
				aggressiveData.add(car);
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
		return aggressiveData;
	}
	
	
	
	
	public List<CarAlert> getCarAlerts() throws SQLException 
	{
		Connection connection = getDBConnection();
        Statement stmt = null;
        List<CarAlert> fuelEffData=new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT vin, message, count(*) as count FROM CARALERT group by vin,message");
            System.out.println("------------------getCarAlerts--------------------");
            while (rs !=null && rs.next()) {
                
            	String vin=rs.getString("vin");
				String message=rs.getString("message");
				String count=rs.getString("count");
            	System.out.println("vin :" +vin +" message :" +message +" count :" +count );
            	CarAlert car=new CarAlert();
				car.setVin(vin);
				car.setMessage(message);
				car.setCount(count);
                fuelEffData.add(car);
            }
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
		return fuelEffData;
	}
	
	

}
