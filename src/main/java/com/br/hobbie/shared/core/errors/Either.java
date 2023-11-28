package com.br.hobbie.shared.core.errors;

import lombok.Getter;

/**
 * <h1>Either implementation to explicit error handling</h1>
 *
 * <p>Either is a data structure that represents two possible values, left or right.
 * This implementation is used to explicit error handling, where the left value represents the error case and the right value represents the success case.</p>
 *
 * <p>Example:</p>
 * <pre>
 *     Either&lt;String, Player&gt; result = playerRepository.findById(id)
 *     .map(Either::right)
 *     .orElseGet(() -> Either.left("Player not found"));
 *
 *     if (result.isLeft()) {
 *      return ResponseEntity.notFound().build();
 *     }
 *
 *     return ResponseEntity.ok(result.getRight());
 * </pre>
 *
 * @param <L> <p>The left value of Either, this value represents the error case</p>
 * @param <R> <p>The right value of Either, this value represents the success case</p>
 */
@Getter
public final class Either<L, R> {
    /**
     * <p>The left value of Either, this value represents the error case</p>
     * -- GETTER --
     * <p>Gets the left value of Either</p>
     * <p>
     * That could be an error message or an exception for example
     * </p>
     */
    private final L left;
    /**
     * <p>The right value of Either, this value represents the success case</p>
     * -- GETTER --
     * <p>Gets the right value of Either</p>
     * <p>
     * That could be the result of a method for example
     * </p>
     */
    private final R right;

    private Either(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * <p>Creates a new Either with the left value</p>
     * <p>
     * That means the method which returns this value has failed
     * </p>
     *
     * @param value <p>The value to be set as left</p>
     * @return <p>A new Either with the left value</p>
     */
    public static <L, R> Either<L, R> left(L value) {
        return new Either<>(value, null);
    }

    /**
     * <p>Creates a new Either with the right value</p>
     * <p>
     * That means the method which returns this value has succeeded
     * </p>
     *
     * @param value <p>The value to be set as right</p>
     * @return <p>A new Either with the right value</p>
     */
    public static <L, R> Either<L, R> right(R value) {
        return new Either<>(null, value);
    }

    /**
     * <p>Checks if the Either has a left value</p>
     * <p>
     * Just in case, if the Either has a left value, it means that the method which returns this value has failed
     * </p>
     *
     * @return <p>True if the Either has a left value, false otherwise</p>
     */
    public boolean isLeft() {
        return left != null;
    }

    /**
     * <p>Checks if the Either has a right value</p>
     * <p>
     * Just in case, if the Either has a right value, it means that the method which returns this value has succeeded
     * </p>
     *
     * @return <p>True if the Either has a right value, false otherwise</p>
     */
    public boolean isRight() {
        return right != null;
    }


}