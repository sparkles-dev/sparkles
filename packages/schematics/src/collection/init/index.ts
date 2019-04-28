import { Rule, externalSchematic } from '@angular-devkit/schematics';

export function initWorkspace(): Rule {

  return (host, context) => {
    context.logger.info(`ng add @nrwl/schematics`);

    // ng add @nrwl/schematics
    return externalSchematic('@nrwl/schematics', 'ng-add', {});
  };
}

export default function schematics(_options: any): Rule {
  return initWorkspace();
}
