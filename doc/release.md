# Release

## Versioning

We use the semantic versioning to manage the software version, with a version format of X.Y.Z (Major.Minor.Patch):

> Given a version number MAJOR.MINOR.PATCH, increment the:
>
> 1. MAJOR version when you make incompatible API changes
> 2. MINOR version when you add functionality in a backwards compatible manner
> 3. PATCH version when you make backwards compatible bug fixes
>
> Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.

For details, see [Semantic Versioning 2.0.0](https://semver.org/#semantic-versioning-200).

## Checklist

Follow these steps before publishing a new release:

- [ ] Pull the latest changes `git pull origin main`.
- [ ] Ensure the working directory is clean (no uncommitted changes) with `git status`.
- [ ] Build locally: `./gradlew clean build`.
- [ ] Create a new tag for the release, following the versioning format, for example:
   ```
   git tag -a 0.1.0 -m "Release version 0.1.0"
   ```
- [ ] Push the tag: `git push origin X.Y.Z`
- [ ] Check the "Actions" and "Releases" tabs on GitHub to confirm the new version was published successfully.
