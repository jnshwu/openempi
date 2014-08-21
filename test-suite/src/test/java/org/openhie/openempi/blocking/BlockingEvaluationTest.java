/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.blocking;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.openhie.openempi.service.BaseServiceTestCase;

public class BlockingEvaluationTest extends BaseServiceTestCase
{
	private Logger log = Logger.getLogger(getClass());
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("The parameters are: gold-standard-links-file blocking-pairs-file");
			return;
		}
		
		BlockingEvaluationTest test = new BlockingEvaluationTest();
		Set<Pair> goldStandard = test.loadGoldStandard(args[0]);
		Set<Pair> blockingPairs = test.loadBlockingPairs(args[1]);
		test.evaluateResults(goldStandard, blockingPairs);
	}

	private void evaluateResults(Set<Pair> goldStandard, Set<Pair> blockingPairs) {
		log.debug("Evaluating " + goldStandard.size() + " known links and " + blockingPairs.size() + " blocking pairs.");
		// Need to find the set of pairs from the gold standard that were captured in the blocking pairs. 
		Map<Pair,Pair> blockingPairsMap = buildMapFromSet(blockingPairs);
		Set<Pair> foundPairs = new HashSet<Pair>();
		Set<Pair> notFoundPairs = new HashSet<Pair>();
		for (Pair pair : goldStandard) {
			if (blockingPairsMap.get(pair) != null) {
				foundPairs.add(pair);
			} else {
				notFoundPairs.add(pair);
			}
		}
		int foundCount = foundPairs.size();
		int notFoundCount = notFoundPairs.size();
		int total = foundCount + notFoundCount;
		
		double pairsCompleteness = ((double) foundCount)/((double) total);
		log.debug("Blocking algorithm captured " + foundCount + " out of " + total + " pairs so, pair-completeness is: " + pairsCompleteness);

		double pairsQuality = 100*((double) foundCount)/((double) blockingPairs.size());
		log.debug("Pairs quality is: " + pairsQuality);
		
		int totalPairs = goldStandard.size()*(goldStandard.size()-1)/2;
		
		double reductionRatio = 1.0 - ((double) total)/((double) totalPairs);
		log.debug("Reduction Ratio is: " + reductionRatio);
	}

	private Map<Pair, Pair> buildMapFromSet(Set<Pair> pairs) {
		Map<Pair,Pair> map = new HashMap<Pair,Pair>();
		for (Pair pair : pairs) {
			map.put(pair, pair);
		}
		return map;
	}

	private Set<Pair> loadGoldStandard(String file) {
		log.debug("Loading gold standard links from file: " + file);
		return loadLinksFromFile(file);
	}

	private Set<Pair> loadBlockingPairs(String file) {
		log.debug("Loading blocking pairs from file: " + file);
		return loadLinksFromFile(file);
	}

	private Set<Pair> loadLinksFromFile(String file) {
		try {
			Scanner sc = new Scanner(new File(file));
			int i=0;
			Set<Pair> pairs = new HashSet<Pair>();
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				i++;
				if (line == null || line.trim().length() == 0) {
					log.trace("Skipping blank line at line " + i);
					continue;
				}
				String[] tokens = line.split(",");
				Pair pair = createPair(tokens);
				pairs.add(pair);
				log.trace("Found pair: " + pair);
			}
			return pairs;
		} catch (Exception e) {
			log.error("Failed while reading the file: " + file + ". Error: " + e, e);
			throw new RuntimeException(e);
		}
	}

	private Pair createPair(String[] tokens) {
		Long left = getPointer(tokens[0]);
		Long right = getPointer(tokens[1]);
		if (left < right) {
			return new Pair(left, right);
		} else {
			return new Pair(right, left);
		}
	}

	private Long getPointer(String token) {
		try {
			if (token == null || token.length() == 0) {
				log.error("The token has an invalid pointer value of: " + token);
				throw new RuntimeException("The token has an invalid pointer value of : " + token);
			}
			return Long.parseLong(token);
		} catch (NumberFormatException e) {
			log.error("Failed to parse the token into a record pointer: " + e, e);
			throw new RuntimeException(e);
		}
	}

	private class Pair
	{
		private Long left;
		private Long right;
		
		public Pair(Long left, Long right) {
			this.left = left;
			this.right = right;
		}

		public Long getLeft() {
			return left;
		}

		public void setLeft(Long left) {
			this.left = left;
		}

		public Long getRight() {
			return right;
		}

		public void setRight(Long right) {
			this.right = right;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((left == null) ? 0 : left.hashCode());
			result = prime * result + ((right == null) ? 0 : right.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (left == null) {
				if (other.left != null)
					return false;
			} else if (!left.equals(other.left))
				return false;
			if (right == null) {
				if (other.right != null)
					return false;
			} else if (!right.equals(other.right))
				return false;
			return true;
		}

		private BlockingEvaluationTest getOuterType() {
			return BlockingEvaluationTest.this;
		}

		@Override
		public String toString() {
			return "Pair [left=" + left + ", right=" + right + "]";
		}
	}
}
