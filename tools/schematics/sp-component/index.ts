import { apply, applyTemplates, chain, externalSchematic, mergeWith, move, url, Rule, SchematicContext, Tree } from '@angular-devkit/schematics';
import { formatFiles } from '@nrwl/schematics/src/utils/rules/format-files';
import { Schema } from './schema';

export default function(schema: Schema): Rule {

  return (host: Tree, context: SchematicContext) => {
    if (!schema.name) {
      context.logger.fatal('Component name must be set!', schema as any);
      throw new Error(`Component name must be set!`);
    }

    const componentName = schema.name;
    const templateSource = apply(url('./files'), [
      applyTemplates(schema),
      move(`packages/components/${componentName}`)
    ]);

    return chain([
      externalSchematic('@schematics/angular', 'module', {
        project: 'components',
        path: `packages/components/${componentName}/src`,
        flat: true,
        commonModule: true
      }),
      externalSchematic('@schematics/angular', 'component', {
        project: 'components',
        path: `packages/components/${componentName}/src`,
        flat: true,
        changeDetection: 'OnPush',
        export: true,
        prefix: 'sp',
        style: 'scss'
      }),
      mergeWith(templateSource),
      formatFiles({
        skipFormat: schema.skipFormat
      })
    ]);
  };
}
