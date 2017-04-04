package audioCompression.algorithm.dsp;

import audioCompression.algorithm.dsp.window.Window;

public class CosineModulatedFilterBank {

	private Filter[] filters;
	private Filter[] decimatedFilters;
	int nBands;
	private Filter prototypeFilter;
	
	public CosineModulatedFilterBank(int nBands, Window w) {
		if(w.getLength()%nBands != 0)
			w.setLength(w.getLength() + (nBands - w.getLength()%nBands));
		this.prototypeFilter = new Filter(0.5f/(2*nBands), w);
		this.nBands = nBands;
		makeFilters();
	}
	
	public Filter[] getFilters() {
		return filters;
	}

	private void makeFilters(){
		float[] h0 = prototypeFilter.getCoefficients();
		int M = h0.length;	// length of prototype filter
		int N = nBands;		// number of subbands
		int L = M/N;		// length of polyphase components
		
		for(int k=0; k<N; k++){
			float[] filter = new float[M];
			double phi_k = (k+0.5)*(L+1)*Math.PI/2.0;
			for(int n=0; n<M; n++){
				double phi = (k+0.5)*(n-(M-1)/2.0)*Math.PI/N;
				filter[n] = (float)(h0[n]*Math.cos(phi+phi_k));
			}
			filters[k] = FilterFactory.makeFilter(filter);
			decimatedFilters[k] = FilterFactory.makeDecimatedFilter(filters[k],M);
		}
	}
	
	
	
}
