package com.gene.bcb.rigb.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPEnvironment;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPLanguage;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;

import com.affymetrix.genometryImpl.SeqSpan;
import com.gene.bcb.rigb.DataAccessException;

public class GRanges {
	
	private REXPReference rexp;
	
	public GRanges(REXPReference rexp) {
		this.rexp = rexp;
	}
	
	public List<String> getSeqNames() throws DataAccessException {
		List<String> seqnames = null;
		try {
			REXPReference seqnamesRef = asVector(callRMethod("seqnames"));
			seqnames = Arrays.asList(seqnamesRef.asStrings());
		} catch (REngineException e) {
			accessorFailed("seqnames", e);
		} catch (REXPMismatchException e) {
			accessorFailed("seqnames", e);
		}
		return seqnames;
	}
	
	public List<Boolean> isForwards() throws DataAccessException {
		List<Boolean> forwards = null;
		try {
			REXPReference strands = asVector(callRMethod("strand"));
			forwards = new ArrayList<Boolean>();
			for(String strand : strands.asStrings()) {
				forwards.add(strand == "+" || strand == "*");
			}
		} catch (REngineException e) {
			accessorFailed("strand", e);
		} catch (REXPMismatchException e) {
			accessorFailed("strand", e);
		}
		return forwards;
	}
	
	public List<Integer> getStarts() throws DataAccessException {
		List<Integer> starts = null;
		try {
			REXPReference startRefs = asVector(callRMethod("start"));
			starts = Arrays.asList(ArrayUtils.toObject(startRefs.asIntegers()));
		} catch (REngineException e) {
			accessorFailed("start", e);
		} catch (REXPMismatchException e) {
			accessorFailed("start", e);
		}
		return starts;
	}
	
	public List<Integer> getEnds() throws DataAccessException {
		List<Integer> ends = null;
		try {
			REXPReference endRefs = asVector(callRMethod("end"));
			ends = Arrays.asList(ArrayUtils.toObject(endRefs.asIntegers()));
		} catch (REngineException e) {
			accessorFailed("end", e);
		} catch (REXPMismatchException e) {
			accessorFailed("end", e);
		}
		return ends;
	}
	
	public Map<String, Integer> getSeqLengths() throws DataAccessException {
		Map<String, Integer> seqlengthMap = null; 
		try {
			REXPReference  seqlengths = asVector(callRMethod("seqlengths"));
			REXPString names = (REXPString) seqlengths.getAttribute("names");
			seqlengthMap = new HashMap<String, Integer>();
			String[] namesArray = names.asStrings();
			int[] seqlengthArray = seqlengths.asIntegers();
			for (int i = 0; i < seqlengths.length(); i++) {
				seqlengthMap.put(namesArray[i], seqlengthArray[i]);
			}
		} catch (REngineException e) {
			accessorFailed("seqlengths", e);
		} catch (REXPMismatchException e) {
			accessorFailed("seqlengths", e);
		}
		return seqlengthMap;
	}
	
	public GRanges subsetByOverlaps(SeqSpan query) throws DataAccessException {
		try {
			return subsetByOverlaps(seqSpanToGRanges(query));
		} catch (Exception e) {
			throw new DataAccessException("Failed to create query GRanges", e);
		}
	}
	public GRanges subsetByOverlaps(GRanges query) throws DataAccessException {
		GRanges subset = null;
		try {
			REXPReference subsetRef = callRFunction("subsetByOverlaps", 
					                                getGenomicRangesNamespace(), 
					                                query.rexp, rexp);
			subset = new GRanges(subsetRef);
		} catch (REngineException e) {
			throw new DataAccessException("R call to 'subsetByOverlaps' failed", e);
		} catch (REXPMismatchException e) {
			throw new DataAccessException("R call to 'subsetByOverlaps' failed", e);
		}
		return subset;
	}
	
	private GRanges seqSpanToGRanges(SeqSpan span) throws REngineException, REXPMismatchException {
		REXPEnvironment ns = getGenomicRangesNamespace();
		REXP strand;
		if (span.isForward())
			strand = new REXPString("+");
		else strand = new REXPString("-");
		REXP iranges = callRFunction("IRanges", ns, new REXPInteger(span.getStart()),
			     				     new REXPInteger(span.getEnd()));
		REXP seqnames = new REXPString(span.getBioSeq().getID());
		REXPReference granges = callRFunction("GRanges", ns, seqnames, iranges, strand);
		return new GRanges(granges);
	}
	
	private REXPReference callRFunction(String name, REXPEnvironment ns, REXP... args) throws REngineException, REXPMismatchException {
		REXP result = null;
		args = ArrayUtils.add(args, 0, getR(name, ns));	
		REXPLanguage lang = new REXPLanguage(new RList(args));
		try {
			result = rexp.getEngine().eval(lang, null, false);
		} catch (REngineException e) {
			throw e;
		} catch (REXPMismatchException e) {
			throw e;
		}
		return (REXPReference)result;
	}
	
	private void accessorFailed(String element, Exception e) throws DataAccessException {
		throw new DataAccessException("Failed to get '" + element + "' from GRanges", e);
	}
	
	private REXP callRMethod(String name, REXP... args) throws REngineException, REXPMismatchException {
		REXPEnvironment ns = getGenomicRangesNamespace();
		return callRFunction(name, ns, ArrayUtils.add(args, 0, rexp));
	}
	
	private REXP getR(String name, REXPEnvironment env) throws REngineException {
		REXP function = null;
		function = env.get(name, true);
		return function;
	}
	
	private REXPReference asVector(REXP obj) throws REngineException, REXPMismatchException {
		return callRFunction("as.vector", null, obj);	
	}
	
	private REXPEnvironment getGenomicRangesNamespace() throws REngineException, REXPMismatchException {
		REXPReference ref = callRFunction("getNamespace", null, new REXPString("GenomicRanges"));
		REXPEnvironment ns = null;
		if (ref.isEnvironment())
			ns = (REXPEnvironment) ref.resolve();
		else throw new REXPMismatchException(ref, "environment");
		return ns;
	}
}
