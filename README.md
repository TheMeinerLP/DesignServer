# Design Server | Simple server to design items, entities or more  

## About this project/server:

This server uses json files to model items, entities and more.
With simple commands, you can then display them on the server.

From the Json files can then be created with the combination of a Gradle plugin, automated classes.  
Thus, it is possible to work with content creator and developer **parallel** and hand in hand. 


Based on:  
![banner](https://github.com/Minestom/Minestom/blob/master/.github/banner.png)


## Examples

### Items
Simple stick with a custom model:
```json
{
  "fileName": "BottleOfDesign",
  "className": "BottleOfDesign",
  "name": "§9BottleOfDesign | 1/3 Gefüllt",
  "packageName": "",
  "amount": 1,
  "material": "minecraft:stick",
  "lore": [
    "§9Das ist eine tolle Flasche von Steffen"
  ],
  "meta": {
    "customModelData": 1,
    "damage": 0,
    "itemFlags": [
      "HIDE_ATTRIBUTES",
      "HIDE_UNBREAKABLE"
    ],
    "enchantments": [
      {
        "enchantment": "minecraft:efficiency",
        "level": 255
      }
    ],
    "attributes": [
      {
        "uuid": "2AD3F246-FEE1-4E67-B886-69FD380BB150",
        "internalName": "justATest",
        "attribute": "generic.armor",
        "operation": "ADDITION",
        "value": 10.0,
        "slot": "HEAD"
      }
    ]
  }
}
```

Field description:   

| Field Name  | Description                 |
|-------------|-----------------------------|
| fileName    | File name of the .kt/java file |   
| className   | Class name in kotlin or java |
| name        | Name means the display name |
| packageName | Means the package name for generation |
| amount      | Means the amount of this item |
| material    | Means the material name since 1.17.1 |
| lore        | Allows to define the lore, supports § chars for colors |
| meta        | Means about the metadata from a item | 
| meta.customModelData | Means the id of the custom model at the resource pack | 
| meta.damage | Means the damage they have a item | 
| meta.itemFlags | Allows to hide some information's from a item | 
| meta.enchantments.enchantment | The name about the enchantment |
| meta.enchantments.level | The level for the enchantment |
| meta.attributes.uuid | The needed uuid for the attribute |
| meta.attributes.internalName | The internal name for the attribute |
| meta.attributes.attribute | The attribute that's be used |
| meta.attributes.operation | The attribute operation that used for the attribute |
| meta.attributes.value | The value for the attribute |
| meta.attributes.slot | The slot for the attribute |

