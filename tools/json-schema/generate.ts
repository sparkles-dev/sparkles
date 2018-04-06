import * as fs from 'fs-extra';
import * as path from 'path';
import { compile, compileFromFile } from 'json-schema-to-typescript'

const SRC_DIR = path.resolve(__dirname, '..', '..', 'libs', 'domain', 'schema');
const OUT_DIR = path.resolve(__dirname, '..', '..', 'libs', 'domain', 'generated');

fs.mkdirp(OUT_DIR);

const FILES = fs.readdirSync(SRC_DIR)

for (const FILE of FILES) {
  const outFile = path.basename(FILE, '.json') + '.d.ts';

  compileFromFile(path.resolve(SRC_DIR, FILE))
    .then(ts => fs.outputFileSync(path.resolve(OUT_DIR, outFile), ts));
}
