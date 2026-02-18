package exercise3

// =============================================================================
// Exercise 3: Task Runner (~20 minutes)
//
// You are building a simple build task runner (think Gradle/Make).
// Tasks can depend on other tasks and must be executed in the correct order.
// All tests in exercise3/TaskRunnerTest.kt must pass.
// =============================================================================

/**
 * Represents a build task with a [name] and a list of [dependsOn] task names.
 */
data class Task(val name: String, val dependsOn: List<String> = emptyList())

/**
 * Exception thrown when a circular dependency is detected.
 */
class CircularDependencyException(message: String) : Exception(message)

/**
 * Exception thrown when a task depends on a task that doesn't exist.
 */
class UnknownTaskException(message: String) : Exception(message)

/**
 * Task 3a: Execution Order
 *
 * Given a list of tasks, return the execution order so that every task
 * runs AFTER all its dependencies have run.
 *
 * Requirements:
 * - Each task appears exactly once in the result
 * - If task A depends on task B, then B appears before A
 * - If a dependency references a task name not in the list,
 *   throw [UnknownTaskException]
 * - If there is a circular dependency, throw [CircularDependencyException]
 * - Among tasks with no ordering constraint, preserve the original order
 *   from the input list (stable topological sort)
 */
fun executionOrder(tasks: List<Task>): List<String> {
    TODO("Implement me")
}

/**
 * Task 3b: Run tasks
 *
 * Given:
 * - A list of [Task] definitions
 * - A [targetTask] name to execute
 * - An [executor] function that "runs" a task by name
 *
 * Execute the target task and all its transitive dependencies in correct order.
 * Only execute tasks that are actually needed (transitive deps of target + target itself).
 * Each task must be executed at most once.
 *
 * Throw [UnknownTaskException] if the target or any dependency is unknown.
 * Throw [CircularDependencyException] if a cycle is detected.
 */
fun runTask(
    tasks: List<Task>,
    targetTask: String,
    executor: (String) -> Unit
) {
    TODO("Implement me")
}
