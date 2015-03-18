package nlp;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class ChineseParser {
	CRFClassifier<CoreLabel> segmenter;
	public void init(String basedir){
		try{
//			System.setOut(new PrintStream(System.out, true, "utf-8"));	
		    Properties props = new Properties();
		    props.setProperty("sighanCorporaDict", basedir);
		    props.setProperty("serDictionary", basedir + "/dict-chris6.ser.gz");
		    props.setProperty("inputEncoding", "UTF-8");
		    props.setProperty("sighanPostProcessing", "true");
	
		    segmenter = new CRFClassifier<CoreLabel>(props);
		    segmenter.loadClassifierNoExceptions(basedir + "/ctb.gz", props);
		    System.out.println(parse("这是一个测试测试测试"));
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	public List<String> parse(String s){
		List<String> segmented = segmenter.segmentString(s);
	    return segmented;
	}
}
