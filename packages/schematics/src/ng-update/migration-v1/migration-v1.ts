import { Rule, SchematicContext, Tree, externalSchematic, chain } from '@angular-devkit/schematics';
import { readJsonInTree } from '../../utils/json';

const updateOrAddNx: Rule = (tree: Tree, context: SchematicContext) => {
  const packageJson = readJsonInTree(tree, 'package.json');

  const inDevDependencies = packageJson['devDependencies'] ? packageJson['devDependencies']['@nrwl/schematics'] : null;
  const inDependencies = packageJson['devDependencies'] ? packageJson['devDependencies']['@nrwl/schematics'] : null;
  if (inDevDependencies || inDependencies) {
    const fromVersion = inDevDependencies || inDependencies;
    const toVersion = '7.7.0';
    context.logger.info(`Migrating @nrwl/schematis from already installed version=${fromVersion} to ${toVersion}`);

    return externalSchematic('@schematics/update', 'update', {
      packages: ['@nrwl/schematics'],
      from: fromVersion,
      to: toVersion,
      force: true
    })
  } else {
    // ng add @nrwl/schematics
    return externalSchematic('@nrwl/schematics', 'ng-add', {});
  }
};

export function migrateToV1(_options: any): Rule {
  return chain([
    updateOrAddNx
  ]);
}
