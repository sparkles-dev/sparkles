import {
  apply,
  applyTemplates,
  chain,
  externalSchematic,
  mergeWith,
  move,
  url,
  Rule,
  SchematicContext,
  Tree
} from '@angular-devkit/schematics';
import { formatFiles } from '@nrwl/workspace/src/utils/rules/format-files';
import { Schema } from './schema';

export default function(schema: Schema): Rule {
  return (host: Tree, context: SchematicContext) => {
    if (!schema.name) {
      context.logger.fatal('Component name must be set!', schema as any);
      throw new Error(`Component name must be set!`);
    }

    const componentName = schema.name;
    const componentPath = `packages/components/${componentName}`;
    const templateSource = apply(url('./files'), [
      applyTemplates(schema),
      move(componentPath)
    ]);

    return chain([
      externalSchematic('@schematics/angular', 'module', {
        project: 'components',
        path: `${componentPath}/src`,
        flat: true,
        commonModule: true
      }),
      externalSchematic('@schematics/angular', 'component', {
        project: 'components',
        path: `${componentPath}/src`,
        flat: true,
        changeDetection: 'OnPush',
        export: true,
        prefix: 'sp',
        style: 'scss'
      }),
      mergeWith(templateSource),
      formatFiles({
        skipFormat: schema.skipFormat
      }),
      () => {
        context.logger.info(
          `Component sp-${componentName} is generated in folder ${componentPath}.`
        );
      }
    ]);
  };
}
