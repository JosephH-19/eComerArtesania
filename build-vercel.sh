#!/bin/sh
set -eu

output_dir="${1:-dist}"
rm -rf "$output_dir"
mkdir -p "$output_dir"

cp -R public/. "$output_dir/"
cp -R admin "$output_dir/admin"
cp -R assets "$output_dir/assets"
cp -R css "$output_dir/css"