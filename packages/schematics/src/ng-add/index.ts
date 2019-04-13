import { Rule, SchematicContext, Tree, externalSchematic, chain } from '@angular-devkit/schematics';

const printHelloWorld: Rule = (tree: Tree, context: SchematicContext) => {
  console.log("Hello world!");

  return tree;
};

// You don't have to export the function as default. You can also have more than one rule factory
// per file.
export default function schematics(_options: any): Rule {
  return chain([
    printHelloWorld,
    externalSchematic('@schematics/update', '@angular/cli', {})
  ]);
}
