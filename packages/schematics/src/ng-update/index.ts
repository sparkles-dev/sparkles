import { Rule, SchematicContext, Tree } from '@angular-devkit/schematics';

export function updateToV1(): Rule {
  return (tree: Tree, _context: SchematicContext) => {
    console.log("Updating to v1!");

    return tree;
  };
}
