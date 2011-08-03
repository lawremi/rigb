package com.gene.bcb.rigb.genometry;

import java.util.List;

import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.SeqSymmetry;
import com.gene.bcb.rigb.DataAccessException;
import com.gene.bcb.rigb.proxy.GRanges;

public interface GRangesToSymmetries {
	// FIXME: Currently, GRanges is not formally associated with a genome identifier,
	// so there is no way of obtaining a AnnotatedSeqGroup. This means we cannot construct
	// any SeqSpans or SeqSymmetries without explicitly providing one.
	public List<? extends SeqSymmetry> convert(GRanges granges, AnnotatedSeqGroup group) throws DataAccessException;
}
