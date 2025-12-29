#!/usr/bin/env bash
set -euo pipefail

OUT="${1:-all_sources_dump.txt}"

# Limpia el fichero de salida
: > "$OUT"

# Recorre todo y solo incluye "text/*" (evita binarios)
# -print0 / read -d '' => seguro con espacios y caracteres raros
find . -type f -print0 | sort -z | while IFS= read -r -d '' f; do
  # Detecta si es texto (incluye Java, properties, yaml, etc.)
  if file -b --mime-type "$f" | grep -q '^text/'; then
    {
      echo "================================================================"
      echo "FILE: ${f#./}"
      echo "================================================================"
      cat "$f"
      echo
      echo
    } >> "$OUT"
  fi
done

echo "OK -> escrito en: $OUT"
