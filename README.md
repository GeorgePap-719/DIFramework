# DI Framework

## Design

**Considerations:**

At initialization, we scan the provided path for annotations. This is done mainly to be able to scan for available
implementations for a given interface in `listOf` API.

Even if we consider solutions without annotations, there is not a (know) way around scanning everything each time.

### Annotation or not

- Spring uses annotation to track dependencies.
- Koin does not require annotations. Though they are provided as an option. But still an explicit path is required.

### Performance

We use a dfs on graph to load a class. An idea to improve this would be to use bfs, since most of the time, the
dependencies are forming a straight line. This way we can greatly improve average and best space complexity.