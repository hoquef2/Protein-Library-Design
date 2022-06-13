package oligoratio;

import com.tomeraberbach.biology.AminoAcid;
import com.tomeraberbach.biology.Codon;
import com.tomeraberbach.biology.DegenerateCodon;
import edu.rit.numeric.NonNegativeLeastSquares;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * A class representing a mapping of amino acids to desired ratios.
 */
public class AminoAcidRatios {
    /**
     * A class representing a solution to the {@link AminoAcidRatios} problem.
     */
    public class Solution {
        /**
         * The map of weights for {@link DegenerateCodon} instances approximating {@link AminoAcidRatios#ratios}.
         * Call {@link Solution#getRatios()} to get outside of this class.
         */
        private Map<DegenerateCodon, Double> ratios;

        /**
         * The map of ratios which indicate the actual yield which will be obtained for each {@link AminoAcid}
         * if {@link Solution#ratios} is used.
         */
        private Map<AminoAcid, Double> yield;

        /**
         * The Euclidean distance of this {@link Solution} from the exact answer.
         * Call {@link Solution#getError()} to get outside of this class.
         */
        private double error;

        /**
         * Constructs a {@link Solution} instance from the given {@code weights} and {@code error}.
         */
        private Solution(Map<DegenerateCodon, Double> ratios, double error) {
            this.ratios = Collections.unmodifiableMap(ratios);
            this.error = error;
            this.yield = new HashMap<>();
            ratios.forEach((degenerateCodon, degenerateCodonRatio) ->
                degenerateCodon.getCodonRatios().forEach((codon, codonRatio) -> {
                    AminoAcid aminoAcid = codon.getAminoAcid();
                    yield.putIfAbsent(aminoAcid, 0.0);
                    yield.put(aminoAcid, yield.get(aminoAcid) + codonRatio * degenerateCodonRatio);
                })
            );
        }

        /**
         * @return the {@link AminoAcidRatios} instance which this is a {@link Solution} for.
         */
        public AminoAcidRatios getProblem() {
            return AminoAcidRatios.this;
        }

        /**
         * @return the map of weights for {@link DegenerateCodon} instances approximating
         * {@link AminoAcidRatios#ratios} (i.e. {@link Solution#ratios}).
         */
        public Map<DegenerateCodon, Double> getRatios() {
            return ratios;
        }

        /**
         * @return The map of ratios which indicate the actual yield which will be obtained for
         * each {@link AminoAcid} if {@link Solution#ratios} is used (i.e. {@link Solution#yield}).
         */
        public Map<AminoAcid, Double> getYield() {
            return yield;
        }

        /**
         * @return the Euclidean distance of this {@link Solution} from the exact answer.
         * (i.e. {@link Solution#error}).
         */
        public double getError() {
            return error;
        }
    }

    /**
     * The desired ratio of each desired {@link AminoAcid}.
     * Call {@link AminoAcidRatios#getRatios()} to get outside of this class.
     */
    private Map<AminoAcid, Double> ratios;

    /**
     * Constructs an {@link AminoAcidRatios} instance from {@code ratios}.
     *
     * @throws IllegalArgumentException if the values of {@code ratios} do not add up to 1.
     */
    public AminoAcidRatios(Map<AminoAcid, Double> ratios) {
        double sum = ratios.values().stream().mapToDouble(r -> r).sum();

        if (Math.abs(sum - 1.0) < 0.0001) {
            this.ratios = Collections.unmodifiableMap(ratios);
        } else {
            throw new IllegalArgumentException(
                "AminoAcidRatios: ratios must add add up to 1.0 (" + sum + ")."
            );
        }
    }

    /**
     * @return the desired ratio of each desired {@link AminoAcid} (i.e. {@link AminoAcidRatios#ratios}).
     */
    public Map<AminoAcid, Double> getRatios() {
        return ratios;
    }

    /**
     * @return the best solution for this {@link AminoAcidRatios} instance using the given {@code degenerateCodons}.
     */
    public Solution solve(Set<DegenerateCodon> degenerateCodons) {
        // Non-negative least squares class for finding an approximate solution to Ax = b
        NonNegativeLeastSquares nnls = new NonNegativeLeastSquares(ratios.size(), degenerateCodons.size());

        // Map from amino acids to their indices in the matrix
        Map<AminoAcid, Integer> aminoAcidIndices = new HashMap<>();

        // Degenerate codons as a list
        List<DegenerateCodon> degenerateCodonList = new ArrayList<>(degenerateCodons);

        // While there is another degenerate codon to process, continue, and keep track of
        // iteration number using i
        for (int i = 0; i < degenerateCodonList.size(); i++) {
            // Current degenerate codon
            DegenerateCodon degenerateCodon = degenerateCodonList.get(i);

            // Gets map of codon ratios
            Map<Codon, Double> codonRatios = degenerateCodon.getCodonRatios();

            // Set of codons expanded from the degeneracies of the current degenerate codon
            Set<Codon> codons = degenerateCodon.getCodons();

            // For each codon expanded from the current degenerate codon
            for (Codon codon : codons) {
                // Amino acid coded for by the current codon
                AminoAcid aminoAcid = codon.getAminoAcid();

                // Assigns this amino acid the next available index in the matrix if it has not been assigned one
                aminoAcidIndices.putIfAbsent(aminoAcid, aminoAcidIndices.size());

                // Updates contribution by the current codon to its amino acid in matrix
                nnls.a[aminoAcidIndices.get(aminoAcid)][i] += codonRatios.get(codon);
            }
        }

        // Sets amino acid target ratios in vector
        aminoAcidIndices.forEach((aminoAcid, index) ->
            nnls.b[index] = ratios.get(aminoAcid)
        );

        // Solves for a non-negative approximate solution of Ax = b   Ax'
        nnls.solve();

        // Sum of numbers in b from Ax = b
        double sum = Arrays.stream(nnls.x).sum();

        // Returns the approximate solution
        return new Solution(
            IntStream.range(0, degenerateCodonList.size())
                .boxed()
                .collect(
                    Collectors.toMap(
                        degenerateCodonList::get,
                        i -> nnls.x[i] / sum
                    )
                ),
            nnls.normsqr
        );
    }

    public Solution solve(double tolerance) {
        List<AminoAcid> aminoAcids = new ArrayList<>(ratios.keySet());

        Solution best = null;
        Stack<Set<DegenerateCodon>> stack = new Stack<>();
        stack.push(Collections.emptySet());

        do {
            Set<DegenerateCodon> degenerateCodons = stack.pop();

            Set<AminoAcid> generatedAminoAcids = degenerateCodons.stream()
                .flatMap(degenerateCodon -> degenerateCodon.getCodons().stream())
                .map(Codon::getAminoAcid)
                .collect(Collectors.toSet());

            if (generatedAminoAcids.equals(ratios.keySet())) {
                Solution solution = solve(degenerateCodons);

                if (best == null || solution.ratios.size() < best.ratios.size() || (solution.ratios.size() == best.ratios.size() && solution.error < best.error)) {
                    best = solution;
                }

                List<DegenerateCodon> degenerateCodonList = new ArrayList<>(degenerateCodons);

                for (int i = 0; i < degenerateCodonList.size(); i++) {
                    for (int j = i + 1; j < degenerateCodonList.size(); j++) {
                        DegenerateCodon first = degenerateCodonList.get(i);
                        DegenerateCodon second = degenerateCodonList.get(j);

                        int differences = 0;
                        for (int k = 0; k < 3; k++) {
                            if (!first.getBases().get(k).equals(second.getBases().get(k))) {
                                differences++;
                            }
                        }

                        if (differences == 1) {
                            Set<DegenerateCodon> copy = new HashSet<>(degenerateCodons);
                            copy.remove(first);
                            copy.remove(second);
                            copy.add(first.merge(second));
                            stack.push(copy);
                        }
                    }
                }
            } else {
                aminoAcids.get(degenerateCodons.size()).getCodons().stream()
                    .map(Codon::toDegenerate)
                    .map(degenerateCodon -> {
                        Set<DegenerateCodon> copy = new HashSet<>(degenerateCodons);
                        copy.add(degenerateCodon);
                        return copy;
                    })
                    .forEach(stack::push);
            }
        } while (!stack.isEmpty() && (best == null || best.error > tolerance));

        return best;
    }

    public Solution solve() {
        return solve(0.01);
    }
}
