package com.gene.bcb.rigb.genometry;

import java.util.ArrayList;
import java.util.List;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.SeqSymmetry;
import com.affymetrix.genometryImpl.SimpleSymWithProps;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleMutableSeqSymmetry;
import com.gene.bcb.rigb.DataAccessException;
import com.gene.bcb.rigb.proxy.GRanges;

public class SimpleGRangesToSymmetries implements GRangesToSymmetries {

	private String scoreColumn;
	
	public SimpleGRangesToSymmetries(String scoreColumn) {
		this.scoreColumn = scoreColumn;
	}
	
	@Override
	public List<? extends SeqSymmetry> convert(GRanges granges, AnnotatedSeqGroup group) throws DataAccessException {
		List<String> seqnames = granges.getSeqNames();
		List<Integer> starts = granges.getStarts();
		List<Integer> ends = granges.getEnds();
		List<Boolean> forwards = granges.isForwards();
		List<SeqSymmetry> symmetries = new ArrayList<SeqSymmetry>(seqnames.size());
		for (int i = 0; i < seqnames.size(); i++) {
			int start = starts.get(i), end = ends.get(i);
			if (forwards.get(i)) {
				int tmpStart = start;
				start = end;
				end = tmpStart;
			}
			MutableSeqSymmetry sym = new SimpleSymWithProps();
			SeqSpan span = new SimpleSeqSpan(start, end, group.getSeq(seqnames.get(i)));
			sym.addSpan(span);
			symmetries.add(sym);
		}
		return symmetries;
	}

}
