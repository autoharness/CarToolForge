# Contributing

Thank you for considering to contribute to `CarToolForge`!

## Found an Issue?

You can create a [new issue](https://github.com/autoharness/CarToolForge/issues/new) if you find a bug in the source code or a mistake in the documentation. Even better you can submit a Pull Request with a fix.

## Contributing Code

This repo uses Github pull requests (PRs) to stage and review code before merging it into the `main` branch. See the [Github docs](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request-from-a-fork) for basics.

Before submitting your pull request, please run the local checks to ensure all tests pass. This helps speed up the review process.

```
./gradlew :buildSrc:spotlessCheck
./gradlew :buildSrc:test
./gradlew clean build
```
