#!/usr/bin/env bash
set -euo pipefail

# Usage: create-github-release.sh <version>

if [[ $# -ne 1 ]]; then
  echo "Usage: $0 <version>" >&2
  exit 1
fi

VERSION="$1"
RELEASE_DIR="build/integration/outputs_to_release"

if [[ ! -d "$RELEASE_DIR" ]]; then
  echo "Error: $RELEASE_DIR does not exist" >&2
  exit 1
fi

shopt -s nullglob
FILES_TO_UPLOAD=("$RELEASE_DIR"/*.zip)
shopt -u nullglob

if [[ ${#FILES_TO_UPLOAD[@]} -eq 0 ]]; then
  echo "Error: release packages not found" >&2
  exit 1
fi

gh release create "$VERSION" \
  "${FILES_TO_UPLOAD[@]}" \
  --title "CarToolForge - $VERSION"
