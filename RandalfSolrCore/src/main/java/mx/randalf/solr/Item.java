/**
 * 
 */
package mx.randalf.solr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;


/**
 * @author massi
 *
 */
public class Item {

	/**
	 * Campo relativo alla Versione necessario per la gestione del motore SolrCloud
	 */
	public static String _VERSION_ ="_version_";

	/**
	 * Chiave utilizzata per la gestione dei documenti Nidificati nell'indice
	 */
	public static String _ROOT_ ="_root_";

	/**
	 * Identificativo univoco della chiave
	 */
	public static String ID = "id";

	private Hashtable<String, ItemFormat> columns;

	/**
	 * 
	 */
	public Item() {
		addColumn(_ROOT_, false, false, false, false, false);
		addColumn(ID, false, false, false, false, false);
	}

	protected void addColumn(String key, boolean multipli, boolean show, boolean sort, boolean kw, boolean fc){
		if (columns == null){
			columns = new Hashtable<String, Item.ItemFormat>();
		}
		columns.put(key, new ItemFormat(key, multipli, show, sort, kw, fc));
	}

	public  void add(SolrInputDocument item, String key, Vector<Object> values){
		ItemFormat itemFormat = null;
		if (columns.get(key)!= null){
			itemFormat = columns.get(key);
			if (!itemFormat.isMultipli()){
				if (values != null) {
					add(item, key, values.get(0), itemFormat.isShow(), itemFormat.isSort(), itemFormat.isKw(), itemFormat.isFc());
				}
			} else {
				if (values != null) {
					for (int x = 0; x < values.size(); x++) {
						add(item, key, values.get(x), itemFormat.isShow(), itemFormat.isSort(), itemFormat.isKw(), itemFormat.isFc());
					}
				}
			}
		}
	}

	public static Params convert(SolrDocument doc){
		Params params = null;
		Iterator<String> fields = null;
		String field = null;
		String field2 = null;
		ArrayList<Object> values = null;

		try{
			params = new Params();
			fields = doc.keySet().iterator();
			while(fields.hasNext()){
				field = fields.next();
				values = (ArrayList<Object>) doc.getFieldValues(field);
				if (field.endsWith("_show")){
					field2 = field.replace("_show", "");
				} else {
					field2 = field;
				}
				if (!field.equals("_rootDesc__show")){
					if (values!= null){
						for(int y=0; y<values.size(); y++){
							params.add(field2, values.get(y));
						}
					} else {
						System.out.println("EEEEE: "+field);
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return params;
	}

	public static void add(SolrInputDocument item, String key, Object value, boolean show, boolean sort, boolean kw, boolean fc){
		String valore = null;
		SimpleDateFormat df = null;
		String vSort = null;

		df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		item.addField(key, value);
		if (value instanceof String){
			valore = (String) value;
		} else if (value instanceof Long){
			valore = ((Long) value).toString();
		} else if (value instanceof Date){
			valore = df.format((Date) value);
		}

		if (show){
			item.addField(key+"_show", valore);
		}
		if (sort){
			vSort = (String) item.getFieldValue(key+"_sort");
			if (vSort!= null){
				item.setField(key+"_sort", vSort +" "+value);
			} else {
				item.addField(key+"_sort", valore);
			}
		}
		if (kw){
			item.addField(key+"_kw", valore);
		}
		if (fc){
			item.addField(key+"_fc", valore.replace(" ", "_"));
		}
	}

	public static void update(SolrInputDocument item, String key, Object value, boolean show, boolean sort, boolean kw, boolean fc){
		String valore = null;
		SimpleDateFormat df = null;

		df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		item.setField(key, value);
		if (value instanceof String){
			valore = (String) value;
		} else if (value instanceof Long){
			valore = ((Long) value).toString();
		} else if (value instanceof Date){
			valore = df.format((Date) value);
		}

		if (show){
			item.setField(key+"_show", valore);
		}
		if (sort){
			item.setField(key+"_sort", valore);
		}
		if (kw){
			item.setField(key+"_kw", valore);
		}
		if (fc){
			item.setField(key+"_fc", valore.replace(" ", "_"));
		}
	}

	class ItemFormat{
		private String key;
		private boolean multipli;
		private boolean show;
		private boolean sort;
		private boolean kw;
		private boolean fc;
		
		public ItemFormat(String key, boolean multipli, boolean show, boolean sort, boolean kw, boolean fc){
			this.key=key;
			this.multipli = multipli;
			this.show = show;
			this.sort = sort;
			this.kw = kw;
			this.fc=fc;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @return the multipli
		 */
		public boolean isMultipli() {
			return multipli;
		}

		/**
		 * @return the show
		 */
		public boolean isShow() {
			return show;
		}

		/**
		 * @return the sort
		 */
		public boolean isSort() {
			return sort;
		}

		/**
		 * @return the kw
		 */
		public boolean isKw() {
			return kw;
		}

		/**
		 * @return the fc
		 */
		public boolean isFc() {
			return fc;
		}
	}
}
