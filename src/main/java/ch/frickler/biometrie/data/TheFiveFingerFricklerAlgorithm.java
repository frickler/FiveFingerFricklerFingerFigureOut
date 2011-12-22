package ch.frickler.biometrie.data;

import java.util.List;


public class TheFiveFingerFricklerAlgorithm {
	private List<Template> templates;
	
	public TheFiveFingerFricklerAlgorithm(List<Template> templates) {
		this.templates = templates;
	}
	
	public void process () {
		int i = 0;
		for (Template t : templates) {
			int j=0;
			for (Template t2 : templates) {
				if (t!=t2) {
					ResultsetByNearest res = new ResultsetByNearest(t);
					ResultsetByNearest res2 = new ResultsetByNearest(t2);
					
					ResultsByTransformation result = new ResultsByTransformation(res.getPairs(), res2.getPairs());
					if (result.getTransformation()!= null) {
						int matchs = result.getMatches();
						if (matchs > 6) {
							int min  = Math.min(res.getPairs().size(),res2.getPairs().size());
							System.out.println(String.format("Template %d and %d are the same with %d matches (%d %%) of %d",i,j,matchs,Math.round((double)matchs/(double)min*100), min));
						}
						
						
					}
				}
				j++;
			}
			i++;
		}
		
	}
	
}
