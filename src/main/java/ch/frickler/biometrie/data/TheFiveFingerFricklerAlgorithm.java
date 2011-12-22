package ch.frickler.biometrie.data;

import java.util.List;


public class TheFiveFingerFricklerAlgorithm {
	private List<Template> templates;
	
	public TheFiveFingerFricklerAlgorithm(List<Template> templates) {
		this.templates = templates;
	}
	
	public void process () {

		for (int i = 0; i < templates.size();i++) {
			Template t = templates.get(i);
			for (int j = i; j < templates.size();j++) {
				Template t2 = templates.get(j);
				if (t!=t2) {
					ResultsetByNearest res = new ResultsetByNearest(t);
					ResultsetByNearest res2 = new ResultsetByNearest(t2);
					
					ResultsByTransformation result = new ResultsByTransformation(res.getPairs(), res2.getPairs());
					if (result.getTransformation()!= null) {
						int matchs = (int)result.getMatches();
						int min  = Math.min(res.getPairs().size(),res2.getPairs().size());
						int percent = (int)Math.round((double)matchs/(double)min*100);
										try{	
						if (percent > 60) {		
							System.out.println(String.format("Template %d and %d are the same with %d matches (%d %%) of %d",i,j,matchs,percent, min));
						}
										}catch(Exception ex){
											
										}
						
						
					}
				}
				j++;
			}
			i++;
		}
		
	}
	
}
