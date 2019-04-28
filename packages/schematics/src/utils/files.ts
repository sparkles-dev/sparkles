import * as fs from 'fs';
import * as path from 'path';
import { ensureDirSync } from 'fs-extra';

export function writeToFile(filePath: string, str: string) {
  ensureDirSync(path.dirname(filePath));
  fs.writeFileSync(filePath, str);
}
