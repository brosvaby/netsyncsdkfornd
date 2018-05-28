package com.inka.netsync.view.widget;

import java.util.List;

final class FixingResult {
	public final boolean fixed;
	public final List<Object> spansWithSpacesBefore;
	public final List<Object> spansWithSpacesAfter;

	public static FixingResult fixed(List<Object> spansWithSpacesBefore, List<Object> spansWithSpacesAfter) {
		return new FixingResult(true, spansWithSpacesBefore, spansWithSpacesAfter);
	}

	public static FixingResult notFixed() {
		return new FixingResult(false, null, null);
	}

	private FixingResult(boolean fixed, List<Object> spansWithSpacesBefore, List<Object> spansWithSpacesAfter) {
		this.fixed = fixed;
		this.spansWithSpacesBefore = spansWithSpacesBefore;
		this.spansWithSpacesAfter = spansWithSpacesAfter;
	}
}