import { Rule, SchematicContext, Tree, externalSchematic, chain } from '@angular-devkit/schematics';
import { NodePackageInstallTask, RunSchematicTask } from '@angular-devkit/schematics/tasks';
import { readJsonInTree, updateJsonInTree } from '../../utils/json';

function updateOrAddNx(): Rule {

  return (tree: Tree, context: SchematicContext) => {
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
}

function addNxToPackageJsonDependencies(): Rule {

  return updateJsonInTree('package.json', (json, context) => {
    context.logger.info(`Updating package.json...`);
    if (!json['devDependencies']) {
      json['devDependencies'] = {};
    }
    json['devDependencies']['@nrwl/schematics'] = '7.7.0';

    return json;
  });
}

function installDependencies(): Rule {
  return (host: Tree, context: SchematicContext) => {
    context.addTask(new NodePackageInstallTask());
    context.logger.info(`🔍 Installing packages...`);

    return host;
  };
}

function scheduleNxAdd(): Rule {
  return (host: Tree, context: SchematicContext) => {
    context.addTask(new RunSchematicTask('@sparkles/schematics', 'init', {}));

    return host;
  };
}


export function migrateToV1(_options: any): Rule {
  return chain([
    addNxToPackageJsonDependencies(),
    installDependencies(),
    scheduleNxAdd()
  ]);
}
