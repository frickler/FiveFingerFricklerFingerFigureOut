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
						int z = result.getMatches();
						if (z > 6) {
							System.out.println(String.format("Template %d and %d are the same with %d matches",i,j,z));
						}
						
						
					}
				}
				j++;
			}
			i++;
		}
		
	}
	
}
