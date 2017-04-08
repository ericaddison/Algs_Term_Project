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
		this.prototypeFilter = FilterFactory.makeLowpassFilter(0.5f/(nBands), w);
		System.out.println("Fiter length = " + w.getLength());
		this.nBands = nBands;
		filters = new Filter[nBands];
		decimatedFilters = new Filter[nBands];
		makeFilters();
	}
	
	public float[][] analysis(float in[]){
		float[][] out = new float[nBands][];
		
		for(int i=0; i<nBands; i++){
			out[i] = filters[i].applyTimeDomain(in);
			out[i] = downsample(out[i], i);
		}
		
		return out;
	}
	
	public float[][] analysisDecimated(float in[]){
		float[][] out = new float[nBands][];
		
		for(int i=0; i<nBands; i++)
			out[i] = decimatedFilters[i].applyTimeDomain(downsample(in, i));
		
		return out;
	}
	
	public float[] synthesis(float in[][]){
		float[] out = new float[in[0].length*in.length];
		
		for(int i=0; i<nBands; i++){
			float[] filt = new float[out.length];
			for(int j=0; j<in[i].length; j++)
				filt[i+j*nBands] = in[i][j];
			
			filt = filters[i].applyTimeDomainReverse(filt);
			
			for(int j=0; j<out.length; j++)
				out[j] += filt[j]/nBands;
		}
		
		return out;
	}	

	public float[] synthesisDecimated(float in[][]){
		float[] out = new float[in[0].length*in.length];
		
		for(int i=0; i<nBands; i++){
			float[] filt = new float[in[i].length];
			filt = decimatedFilters[i].applyTimeDomainReverse(in[i]);
			
			// upsample
			for(int j=0; j<in[i].length; j++)
				out[i+j*nBands] = filt[j];
		}
		
		return out;
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
				double phi = (k+0.5)*(n-(M-1)/2.0)*Math.PI/(N);
				filter[n] = (float)(h0[n]*Math.cos(phi+phi_k));
			}
			filters[k] = FilterFactory.makeFilter(filter);
			decimatedFilters[k] = FilterFactory.makeDecimatedFilter(filters[k],N,k);
		}
	}
	
	
	public float[] downsample(float[] inArray, int offset){
		int M = nBands;
		
		float[] outArray = new float[inArray.length/M];
		
		for(int j=0; j<outArray.length; j++)
			outArray[j] = inArray[offset + j*M];
		
		return outArray;
	}
	
	
}
