"use strict";(self.webpackChunkrosetta_docs=self.webpackChunkrosetta_docs||[]).push([[138],{5142:(e,n,a)=>{a.r(n),a.d(n,{assets:()=>c,contentTitle:()=>d,default:()=>h,frontMatter:()=>i,metadata:()=>t,toc:()=>r});const t=JSON.parse('{"id":"legacy-docs/rosetta-custom-definitions","title":"Rosetta Custom Definitions","description":"Custom definitions used in the legacy Cardano Rosetta implementation","source":"@site/docs/legacy-docs/rosetta-custom-definitions.md","sourceDirName":"legacy-docs","slug":"/legacy-docs/rosetta-custom-definitions","permalink":"/cardano-rosetta-java/docs/legacy-docs/rosetta-custom-definitions","draft":false,"unlisted":false,"editUrl":"https://github.com/cardano-foundation/cardano-rosetta-java/tree/main/docs/docs/legacy-docs/rosetta-custom-definitions.md","tags":[],"version":"current","sidebarPosition":5,"frontMatter":{"sidebar_position":5,"title":"Rosetta Custom Definitions","description":"Custom definitions used in the legacy Cardano Rosetta implementation"},"sidebar":"tutorialSidebar","previous":{"title":"Pool Operations Support","permalink":"/cardano-rosetta-java/docs/legacy-docs/pool-operations-support"},"next":{"title":"Staking Support","permalink":"/cardano-rosetta-java/docs/legacy-docs/staking-support"}}');var s=a(4848),o=a(8453);const i={sidebar_position:5,title:"Rosetta Custom Definitions",description:"Custom definitions used in the legacy Cardano Rosetta implementation"},d="Introduction",c={},r=[{value:"<code>/construction/derive</code>",id:"constructionderive",level:2},{value:"Examples",id:"examples",level:3},{value:"Base address",id:"base-address",level:4},{value:"Reward address",id:"reward-address",level:4},{value:"Enterprise address",id:"enterprise-address",level:3},{value:"<code>/account/balance</code>",id:"accountbalance",level:2},{value:"Response",id:"response",level:3},{value:"<code>/block</code>",id:"block",level:2},{value:"Request",id:"request",level:3},{value:"Response",id:"response-1",level:3},{value:"<code>/block/transactions</code>",id:"blocktransactions",level:2},{value:"Response",id:"response-2",level:3},{value:"<code>/construction/preprocess</code>",id:"constructionpreprocess",level:2},{value:"Request",id:"request-1",level:3},{value:"Response",id:"response-3",level:3},{value:"<code>/contruction/metadata</code>",id:"contructionmetadata",level:2},{value:"Request",id:"request-2",level:3},{value:"Response",id:"response-4",level:3},{value:"<code>/construction/payloads</code>",id:"constructionpayloads",level:2},{value:"Request",id:"request-3",level:3},{value:"<code>/construction/parse</code>",id:"constructionparse",level:2},{value:"<code>/construction/combine</code>",id:"constructioncombine",level:2},{value:"Encoded transactions",id:"encoded-transactions",level:2},{value:"<code>/search/transactions</code>",id:"searchtransactions",level:2}];function l(e){const n={a:"a",blockquote:"blockquote",code:"code",em:"em",h1:"h1",h2:"h2",h3:"h3",h4:"h4",header:"header",li:"li",p:"p",pre:"pre",ul:"ul",...(0,o.R)(),...e.components};return(0,s.jsxs)(s.Fragment,{children:[(0,s.jsx)(n.header,{children:(0,s.jsx)(n.h1,{id:"introduction",children:"Introduction"})}),"\n",(0,s.jsxs)(n.p,{children:["Although ",(0,s.jsx)(n.code,{children:"Cardano Rosetta"})," is compliant with ",(0,s.jsx)(n.a,{href:"https://rosetta-api.org/",children:"Rosetta Spec"}),", some changed were added, mostly as metadata, as they contain Cardano specific information that needs to be either processed or returned."]}),"\n",(0,s.jsx)(n.h1,{id:"endpoint-specific-changes",children:"Endpoint specific changes"}),"\n",(0,s.jsx)(n.h2,{id:"constructionderive",children:(0,s.jsx)(n.code,{children:"/construction/derive"})}),"\n",(0,s.jsx)(n.p,{children:"By default this endpoint creates an Enterprise address but Cardano Rosetta also allows the creation of Reward and Base addresses, which aren't supported in the Rosetta specification. Therefore, following optional parameters are sent as metadata:"}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.code,{children:"address_type"}),': either "Reward", "Base" or "Enterprise". It will default to "Enterprise" and will throw an error if any other value is provided. These types are explained in the section 4 of the ',(0,s.jsx)(n.a,{href:"https://hydra.iohk.io/build/3671214/download/1/ledger-spec.pdf",children:"ledger specification"}),"."]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.code,{children:"staking_credential"}),": the public key that will be used for creating a Base address and the format will be the same as the public key. This field is only mandatory if the provided ",(0,s.jsx)(n.code,{children:"address_type"}),' is "Base". It\'s ignored in other cases since the Reward and the Enterprise addresses are created with the public key already included in the request.']}),"\n"]}),"\n",(0,s.jsx)(n.h3,{id:"examples",children:"Examples"}),"\n",(0,s.jsx)(n.h4,{id:"base-address",children:"Base address"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "network_identifier": { "blockchain": "cardano", "network": "mainnet" },\n  "public_key": {\n    "hex_bytes": "159abeeecdf167ccc0ea60b30f9522154a0d74161aeb159fb43b6b0695f057b3",\n    "curve_type": "edwards25519"\n  },\n  "metadata": {\n    "address_type": "Base",\n    "staking_credential": {\n      "hex_bytes": "964774728c8306a42252adbfb07ccd6ef42399f427ade25a5933ce190c5a8760",\n      "curve_type": "edwards25519"\n    }\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h4,{id:"reward-address",children:"Reward address"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "network_identifier": { "blockchain": "cardano", "network": "mainnet" },\n  "public_key": {\n    "hex_bytes": "964774728c8306a42252adbfb07ccd6ef42399f427ade25a5933ce190c5a8760",\n    "curve_type": "edwards25519"\n  },\n  "metadata": { "address_type": "Reward" }\n}\n'})}),"\n",(0,s.jsx)(n.h3,{id:"enterprise-address",children:"Enterprise address"}),"\n",(0,s.jsxs)(n.p,{children:["In this case the metadata is optional. If it's provided, then the ",(0,s.jsx)(n.code,{children:"address_type"}),' should be "Enterprise" and the ',(0,s.jsx)(n.code,{children:"staking_credential"})," could be anything since it will be ignored."]}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "network_identifier": { "blockchain": "cardano", "network": "mainnet" },\n  "public_key": {\n    "hex_bytes": "1B400D60AAF34EAF6DCBAB9BBA46001A23497886CF11066F7846933D30E5AD3F",\n    "curve_type": "edwards25519"\n  },\n  "metadata": {\n    "address_type": "Enterprise",\n    "staking_credential": {\n      "hex_bytes": "1B400D60AAF34EAF6DCBAB9BBA46001A23497886CF11066F7846933D30E5AD3F__",\n      "curve_type": "edwards25519"\n    }\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h2,{id:"accountbalance",children:(0,s.jsx)(n.code,{children:"/account/balance"})}),"\n",(0,s.jsxs)(n.p,{children:["For accounts that have a multi asset balance there will be returned with the corresponding policy passed as metadata at ",(0,s.jsx)(n.code,{children:"currency"})," as follows:"]}),"\n",(0,s.jsx)(n.h3,{id:"response",children:"Response"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-typescript",children:'{\n  "block_identifier": {\n    "index": 382733,\n    "hash": "50bb3491000528b19a074291bd958b77dd0b8b1cf3003bf14d1ac24a62073f1e"\n  },\n  "balances": [\n    { "value": "4800000", "currency": { "symbol": "ADA", "decimals": 6 } },\n   {\n      "value": "20",\n      "currency": {\n        "symbol": "",                                                               // token symbol as hex string\n        "decimals": 0,\n        "metadata": {\n          "policyId": "181aace621eea2b6cb367adb5000d516fa785087bad20308c072517e"    // token policy as hex string\n        }\n      }\n    },\n    {\n      "value": "10",\n      "currency": {\n        "symbol": "7376c3a57274",\n        "decimals": 0,\n        "metadata": {\n          "policyId": "fc5a8a0aac159f035a147e5e2e3eb04fa3b5e67257c1b971647a717d"\n        }\n      }\n    }\n  ],\n  "coins": [...]\n}\n'})}),"\n",(0,s.jsxs)(n.p,{children:["Also, ",(0,s.jsx)(n.code,{children:"coins"})," will be returned with the token bundle list corresponding to each coin as metadata as follows:"]}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-typescript",children:'{\n  "block_identifier": {\n    "index": 382733,\n    "hash": "50bb3491000528b19a074291bd958b77dd0b8b1cf3003bf14d1ac24a62073f1e"\n  },\n  "balances": [...],\n  "coins": [\n    {\n      "coin_identifier": {\n        "identifier": "02562c123f6d560e1250f5a46f7e95911b21fd8a9fa70157335c3a3d1d16bdda:0"\n      },\n      "amount": {\n        "currency": { "decimals": 6, "symbol": "ADA" },\n        "value": "4800000"\n      },\n      "metadata": {\n        "02562c123f6d560e1250f5a46f7e95911b21fd8a9fa70157335c3a3d1d16bdda:0": [       // coin identifier\n          {\n            "policyId": "181aace621eea2b6cb367adb5000d516fa785087bad20308c072517e",\n            "tokens": [\n              {\n                "value": "20",\n                "currency": {\n                  "decimals": 0,\n                  "symbol": "",\n                  "metadata": {\n                    "policyId": "181aace621eea2b6cb367adb5000d516fa785087bad20308c072517e"\n                  }\n                }\n              }\n            ]\n          },\n          {\n            "policyId": "fc5a8a0aac159f035a147e5e2e3eb04fa3b5e67257c1b971647a717d",\n            "tokens": [\n              {\n                "value": "10",\n                "currency": {\n                  "decimals": 0,\n                  "symbol": "7376c3a57274",\n                  "metadata": {\n                    "policyId": "fc5a8a0aac159f035a147e5e2e3eb04fa3b5e67257c1b971647a717d"\n                  }\n                }\n              }\n            ]\n          }\n        ]\n      }\n    }\n  ]\n}\n'})}),"\n",(0,s.jsx)(n.h2,{id:"block",children:(0,s.jsx)(n.code,{children:"/block"})}),"\n",(0,s.jsx)(n.p,{children:"The following metadata is also returned when querying for block information:"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-typescript",children:'"transactionsCount": { "type": "number" }, // amount of transactions in the block\n"createdBy": { "type": "string" },         // block creation time in UTC expressed as linux timestamp\n"size": { "type": "number" },              // block size in bytes\n"epochNo": { "type": "number" },           // epoch where the block has been included\n"slotNo": { "type": "number" }             // block slot number\n'})}),"\n",(0,s.jsx)(n.h3,{id:"request",children:"Request"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "network_identifier": { "blockchain": "cardano", "network": "mainnet" },\n  "block_identifier": {\n    "index": 100\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h3,{id:"response-1",children:"Response"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "block": {\n    "block_identifier": {\n      "hash": "a52cca923a67326ea9c409e958a17a77990be72f3607625ec5b3d456202e223e",\n      "index": 100\n    },\n    "metadata": {\n      "createdBy": "ByronGenesis-52df0f2c5539b2b1",\n      "epochNo": 0,\n      "size": 667,\n      "slotNo": 99,\n      "transactionsCount": 0\n    },\n    "parent_block_identifier": {\n      "hash": "3d081d225a34a7ead8f12f8f7458a4994e40dc56322654abc04f41c8bb26c723",\n      "index": 99\n    },\n    "timestamp": 1506205071000,\n    "transactions": []\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h2,{id:"blocktransactions",children:(0,s.jsx)(n.code,{children:"/block/transactions"})}),"\n",(0,s.jsx)(n.p,{children:"When the block requested contains transactions with multi assets operations the token bundles associated to each operation will be returned as metadata as follows:"}),"\n",(0,s.jsx)(n.p,{children:(0,s.jsx)(n.em,{children:"Also, assets will be returned sorted by name."})}),"\n",(0,s.jsx)(n.h3,{id:"response-2",children:"Response"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-typescript",children:'{\n  "transaction_identifier": {\n    "hash": "2356072a5379064aa62f83bf61d7d4467dbc47ec281461b558aa51b08c38c884"\n  },\n  "operations": [\n    {\n      "operation_identifier": {\n        "index": 0\n      },\n      "type": "input",\n      "status": "success",\n      "account": {\n        "address": "addr_test1vpcv26kdu8hr9x939zktp275xhwz4478c8hcdt7l8wrl0ecjftnfa"\n      },\n      "amount": {\n        "value": "-999999000000",\n        "currency": {\n          "symbol": "ADA",\n          "decimals": 6\n        }\n      },\n      "coin_change": {\n        "coin_identifier": {\n          "identifier": "127cf01f95448cdcde439b8ace9b8a8ec100e690abe2b52069b3dbd924e032b3:0"\n        },\n        "coin_action": "coin_spent"\n      },\n      "metadata": {\n        "tokenBundle": [\n          {\n            "policyId": "3e6fc736d30770b830db70994f25111c18987f1407585c0f55ca470f",\n            "tokens": [\n              {\n                "value": "-5",\n                "currency": {\n                  "symbol": "6a78546f6b656e31",\n                  "decimals": 0\n                }\n              }\n            ]\n          }\n        ]\n      }\n    },\n    ...\n  ]\n}\n'})}),"\n",(0,s.jsx)(n.h2,{id:"constructionpreprocess",children:(0,s.jsx)(n.code,{children:"/construction/preprocess"})}),"\n",(0,s.jsxs)(n.p,{children:["Not only input and output operations are allowed but also special staking operations as described in ",(0,s.jsx)(n.a,{href:"/cardano-rosetta-java/docs/legacy-docs/staking-support",children:"here"}),"."]}),"\n",(0,s.jsxs)(n.p,{children:["It is possible to operate with multi assets tokens too, as explained ",(0,s.jsx)(n.a,{href:"/cardano-rosetta-java/docs/legacy-docs/multi-assets-support",children:"here"}),"."]}),"\n",(0,s.jsxs)(n.p,{children:["Cardano transactions require a ",(0,s.jsx)(n.code,{children:"ttl"})," to be defined. As it's explained ",(0,s.jsx)(n.a,{href:"https://docs.cardano.org/projects/cardano-node/en/latest/reference/building-and-signing-tx.html",children:"in the cardano docs"}),":"]}),"\n",(0,s.jsxs)(n.blockquote,{children:["\n",(0,s.jsx)(n.p,{children:"Time-to-live (TTL) - represents a slot, or deadline by which a transaction must be submitted. The TTL is an absolute slot number, rather than a relative one, which means that the \u2013ttl value should be greater than the current slot number. A transaction becomes invalid once its ttl expires."}),"\n"]}),"\n",(0,s.jsxs)(n.p,{children:["There are several restrictions that require a more complex workflow when defining ",(0,s.jsx)(n.code,{children:"ttl"})," for a transaction:"]}),"\n",(0,s.jsxs)(n.ul,{children:["\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.code,{children:"ttl"})," depends on the latest block slot number and Rosetta spec only allows online data to be fetched in ",(0,s.jsx)(n.code,{children:"/construction/metadata"}),"."]}),"\n",(0,s.jsxs)(n.li,{children:[(0,s.jsx)(n.code,{children:"/construction/metadata"})," only accepts parameters produced, without any modifications, by ",(0,s.jsx)(n.code,{children:"/construction/preprocess"}),"."]}),"\n"]}),"\n",(0,s.jsxs)(n.p,{children:["To be able to stay compliant with Rosetta spec but also be able to let consumers to configure ",(0,s.jsx)(n.code,{children:"ttl"})," a new parameter was introduced:"]}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-typescript",children:"metadata?: {\n  relative_ttl: number;\n};\n"})}),"\n",(0,s.jsxs)(n.p,{children:["If no ",(0,s.jsx)(n.code,{children:"relative_ttl"})," is sent, a default one, ",(0,s.jsx)(n.code,{children:"DEFAULT_RELATIVE_TTL"}),", will be returned."]}),"\n",(0,s.jsx)(n.h3,{id:"request-1",children:"Request"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "network_identifier": {\n    "blockchain": "cardano",\n    "network": "mainnet"\n  },\n  "operations": [...],\n  "metadata": {\n    "relative_ttl": 1000\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h3,{id:"response-3",children:"Response"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "options": {\n    "relative_ttl": 1000,\n    "transaction_size": 298\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h2,{id:"contructionmetadata",children:(0,s.jsx)(n.code,{children:"/contruction/metadata"})}),"\n",(0,s.jsxs)(n.p,{children:["Metadata endpoint needs to receive the ",(0,s.jsx)(n.code,{children:"relative_ttl"})," returned in process so it can calculate the actual ",(0,s.jsx)(n.code,{children:"ttl"})," based on latest block slot number."]}),"\n",(0,s.jsx)(n.h3,{id:"request-2",children:"Request"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "network_identifier": {\n    "blockchain": "cardano",\n    "network": "mainnet"\n  },\n  "options": {\n    "relative_ttl": 1000,\n    "transaction_size": 298\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h3,{id:"response-4",children:"Response"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "metadata": {\n    "ttl": "65294",\n    "suggested_fee": [\n      {\n        "currency": {\n          "decimals": 6,\n          "symbol": "ADA"\n        },\n        "value": "900000"\n      }\n    ]\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h2,{id:"constructionpayloads",children:(0,s.jsx)(n.code,{children:"/construction/payloads"})}),"\n",(0,s.jsxs)(n.p,{children:["Not only input and output operations are allowed but also special staking operations as described in ",(0,s.jsx)(n.a,{href:"/cardano-rosetta-java/docs/legacy-docs/staking-support",children:"here"}),"."]}),"\n",(0,s.jsxs)(n.p,{children:["It is possible to operate with multi assets tokens too, as explained ",(0,s.jsx)(n.a,{href:"/cardano-rosetta-java/docs/legacy-docs/multi-assets-support",children:"here"}),"."]}),"\n",(0,s.jsxs)(n.p,{children:["If it is required to send a Catalyst vote registration, the information needed to do that can be found ",(0,s.jsx)(n.a,{href:"/cardano-rosetta-java/docs/legacy-docs/catalyst-vote-support",children:"here"})]}),"\n",(0,s.jsxs)(n.p,{children:["Furthermore, transaction ",(0,s.jsx)(n.code,{children:"ttl"})," needs to be sent as string in the metadata."]}),"\n",(0,s.jsx)(n.h3,{id:"request-3",children:"Request"}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "network_identifier": {\n    "blockchain": "cardano",\n    "network": "mainnet"\n  },\n  "operations": [...],\n  "metadata": {\n    "ttl": "65294"\n  }\n}\n'})}),"\n",(0,s.jsx)(n.h2,{id:"constructionparse",children:(0,s.jsx)(n.code,{children:"/construction/parse"})}),"\n",(0,s.jsxs)(n.p,{children:["The request of this endpoint has no specific change but the response will have the operations parsed in the same way as the ones that are used to send as payload in the ",(0,s.jsx)(n.code,{children:"/construction/payloads"})," and ",(0,s.jsx)(n.code,{children:"/construction/preprocess"})," endpoints. This means that if the order used in those two endpoints needs to be exactly the one specified ",(0,s.jsx)(n.a,{href:"/cardano-rosetta-java/docs/legacy-docs/staking-support",children:"here"}),". Otherwise the parse endpoint will not be able to reproduce the operations in the same order and the workflow will fail."]}),"\n",(0,s.jsx)(n.h2,{id:"constructioncombine",children:(0,s.jsx)(n.code,{children:"/construction/combine"})}),"\n",(0,s.jsxs)(n.p,{children:["In order to support Byron addresses an extra field called ",(0,s.jsx)(n.code,{children:"chain_code"})," in the ",(0,s.jsx)(n.code,{children:"account_identifier"}),"'s ",(0,s.jsx)(n.code,{children:"metadata"})," of the corresponding ",(0,s.jsx)(n.code,{children:"signing_payload"})," must be added when requesting to sign payloads. So, the payload would look something like this:"]}),"\n",(0,s.jsx)(n.pre,{children:(0,s.jsx)(n.code,{className:"language-json",children:'{\n  "network_identifier": {\n    "blockchain": "cardano",\n    "network": "mainnet"\n  },\n  "unsigned_transaction": "00000000000000000000000000",\n  "signatures": [\n    {\n      "signing_payload": {\n        "account_identifier": {\n          "address": "addr1vxa5pudxg77g3sdaddecmw8tvc6hmynywn49lltt4fmvn7cpnkcpx",\n          "metadata": {\n            "chain_code": "dd75e154da417becec55cdd249327454138f082110297d5e87ab25e15fad150f"\n          }\n        },\n        "hex_bytes": "31fc9813a71d8db12a4f2e3382ab0671005665b70d0cd1a9fb6c4a4e9ceabc90",\n        "signature_type": "ed25519"\n      },\n      "public_key": {\n        "hex_bytes": "1B400D60AAF34EAF6DCBAB9BBA46001A23497886CF11066F7846933D30E5AD3F",\n        "curve_type": "edwards25519"\n      },\n      "signature_type": "ed25519",\n      "hex_bytes": "00000000000000000000000000"\n    }\n  ]\n}\n'})}),"\n",(0,s.jsxs)(n.p,{children:["This value can be obtained by any of the Bip 32 Keys. More information can be found (here)[",(0,s.jsx)(n.a,{href:"https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki#extended-keys",children:"https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki#extended-keys"}),"].","\nThe creation of these type of addresses are not supported by Rosetta, to do so this link can be (useful)[",(0,s.jsx)(n.a,{href:"https://github.com/Emurgo/cardano-serialization-lib/blob/master/doc/getting-started/generating-keys.md#use-in-addresses",children:"https://github.com/Emurgo/cardano-serialization-lib/blob/master/doc/getting-started/generating-keys.md#use-in-addresses"}),"]."]}),"\n",(0,s.jsx)(n.h1,{id:"other-changes",children:"Other changes"}),"\n",(0,s.jsx)(n.h2,{id:"encoded-transactions",children:"Encoded transactions"}),"\n",(0,s.jsxs)(n.p,{children:["Both ",(0,s.jsx)(n.code,{children:"signed_unsigned"})," and ",(0,s.jsx)(n.code,{children:"unsigned_transaction"})," don't correspond to a valid Cardano Transaction that can be forwarded to the network as they contain extra data required in the Rosetta workflow. This means that such transactions cannot be decoded nor sent directly to a ",(0,s.jsx)(n.code,{children:"cardano-node"}),"."]}),"\n",(0,s.jsxs)(n.p,{children:["The rationale behind that decision can be found ",(0,s.jsx)(n.a,{href:"https://community.rosetta-api.org/t/implementing-the-construction-api-for-utxo-model-coins/100/3",children:"here"}),":"]}),"\n",(0,s.jsxs)(n.blockquote,{children:["\n",(0,s.jsx)(n.p,{children:'The best way to get around this is for /construction/payloads to return additional metadata about the transaction which you may need later on for combining & parsing and wrapping the raw unsigned/signed transaction with this metadata. For example, in the UnsignedTransaction field of ConstructionPayloadsResponse, you can return a "rich" Sia transaction, which has additional info you need in /construction/combine.'}),"\n"]}),"\n",(0,s.jsxs)(n.blockquote,{children:["\n",(0,s.jsx)(n.p,{children:"[..] There is no expectation that the transactions which are constructed in Rosetta can be parsed by network-specific tools or broadcast on a non-Rosetta node. All parsing and broadcast of these transactions will occur exclusively over the Rosetta API."}),"\n"]}),"\n",(0,s.jsx)(n.p,{children:"The same approach has been used to encode the operations that contain a staking key, since they couldn't be decoded otherwise."}),"\n",(0,s.jsx)(n.p,{children:"Transaction's metadata, needed for example for vote registration operations, is also encoded as extra data."}),"\n",(0,s.jsx)(n.h2,{id:"searchtransactions",children:(0,s.jsx)(n.code,{children:"/search/transactions"})}),"\n",(0,s.jsxs)(n.p,{children:["This API can be disabled by setting ",(0,s.jsx)(n.code,{children:"DISABLE_SEARCH_API"})," env variable to ",(0,s.jsx)(n.code,{children:"t"}),", ",(0,s.jsx)(n.code,{children:"true"})," or 1"]}),"\n",(0,s.jsxs)(n.p,{children:["Max amount of transactions allowed to be requested is defined by ",(0,s.jsx)(n.code,{children:"PAGE_SIZE"})," env variable, which is the same used at ",(0,s.jsx)(n.code,{children:"/block/transaction"})," endpoint. Also, this value will be used if no limit parameter is received."]}),"\n",(0,s.jsxs)(n.p,{children:["How it works:\n",(0,s.jsx)(n.code,{children:"status"})," and ",(0,s.jsx)(n.code,{children:"success"})," filters are equivalent. If they are both setted and they don't match an error will be thrown. In the same way works ",(0,s.jsx)(n.code,{children:"address"})," and ",(0,s.jsx)(n.code,{children:"account_identifier.address"}),".\n",(0,s.jsx)(n.code,{children:"status"})," and ",(0,s.jsx)(n.code,{children:"maxBlock"}),"filters work as excluding filters, if they are setted, besides operator value."]})]})}function h(e={}){const{wrapper:n}={...(0,o.R)(),...e.components};return n?(0,s.jsx)(n,{...e,children:(0,s.jsx)(l,{...e})}):l(e)}},8453:(e,n,a)=>{a.d(n,{R:()=>i,x:()=>d});var t=a(6540);const s={},o=t.createContext(s);function i(e){const n=t.useContext(o);return t.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function d(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(s):e.components||s:i(e.components),t.createElement(o.Provider,{value:n},e.children)}}}]);