import { Rule, SchematicContext, Tree, externalSchematic, chain } from '@angular-devkit/schematics';
import { NodePackageInstallTask, RunSchematicTask } from '@angular-devkit/schematics/tasks';
import { updateJsonInTree } from '../../utils/json';
import { VERSIONS } from '../../versions';

function addNxToPackageJsonDependencies(): Rule {

  return updateJsonInTree('package.json', (json, context) => {
    context.logger.info(`Updating package.json...`);
    if (!json['devDependencies']) {
      json['devDependencies'] = {};
    }
    json['devDependencies']['@nrwl/schematics'] = VERSIONS.NX;

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

// schematic function for `ng add @sparkles/schematics`
export default function schematics(_options: any): Rule {
  return chain([
    addNxToPackageJsonDependencies(),
    installDependencies(),
    scheduleNxAdd(),
  ]);
}
