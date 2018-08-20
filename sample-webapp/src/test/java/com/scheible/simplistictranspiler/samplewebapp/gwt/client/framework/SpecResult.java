package com.scheible.simplistictranspiler.samplewebapp.gwt.client.framework;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sj
 */
class SpecResult {

	enum Status {
		PASSED, FAILED
	}

	static class FailedExpectation {

		String matcherName;
		String message;

		public void setMatcherName(String matcherName) {
			this.matcherName = matcherName;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + "[matcherName=" + matcherName + ",message=" + message + "]";
		}
	}

	List<FailedExpectation> failedExpectations = new ArrayList<>();
	String id;
	Status status;

	void setDescription(String description) {
		this.id = description;
	}

	void setStatus(Status status) {
		this.status = status;
	}

	public void setFailedExpectations(List<FailedExpectation> failedExpectations) {
		this.failedExpectations = failedExpectations;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + ",status=" + status + ",failedExpectations=" + failedExpectations + "]";
	}
}
