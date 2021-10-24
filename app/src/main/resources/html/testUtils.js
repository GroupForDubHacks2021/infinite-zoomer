
/**
 * Various utilities for writing JavaScript tests.
 */

function assert_eq(a, b, message) {
    if (a !== b) {
        throw `ASSERTION ERROR: ${a} !== ${b}. Message: ${message}`;
    }
}

export { assert_eq };
