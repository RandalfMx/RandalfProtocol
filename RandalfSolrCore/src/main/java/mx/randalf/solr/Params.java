/**
 * 
 */
package mx.randalf.solr;

import java.math.BigInteger;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author massi
 *
 */
public class Params {
	private Hashtable<String, List<Object>> params;
	
	public Params(){
		params = new Hashtable<String, List<Object>>();
	}

	public void add(String key, Object value) throws ClassCastException{
		if (value.getClass().getName().equals(String.class.getName())){
			add(key, (String)value);
		} else if (value.getClass().getName().equals(Date.class.getName())){
			add(key, (Date)value);
		} else if (value.getClass().getName().equals(BigInteger.class.getName())){
			add(key, (BigInteger)value);
		} else if (value.getClass().getName().equals(Long.class.getName())){
			add(key, (Long)value);
		} else {
			throw new ClassCastException("Il formato ["+value.getClass().getName()+"] non gestito");
		} 
	}

	public void add(String key, String value){
		List<Object> values = null;
		
		if (params.get(key)== null){
			values = new Vector<Object>();
		} else {
			values= params.get(key);
		}
		values.add(value);
		params.put(key, values);
	}

	public void add(String key, Date value){
		List<Object> values = null;
		
		if (params.get(key)== null){
			values = new Vector<Object>();
		} else {
			values= params.get(key);
		}
		values.add(value);
		params.put(key, values);
	}

	public void add(String key, BigInteger value){
		List<Object> values = null;
		
		if (params.get(key)== null){
			values = new Vector<Object>();
		} else {
			values= params.get(key);
		}
		values.add(value.toString());
		params.put(key, values);
	}

	public void add(String key, Long value){
		List<Object> values = null;
		
		if (params.get(key)== null){
			values = new Vector<Object>();
		} else {
			values= params.get(key);
		}
		values.add(value);
		params.put(key, values);
	}

	public Hashtable<String, List<Object>> getParams() {
		return params;
	}

	
}