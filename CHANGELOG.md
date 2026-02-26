# Changelog

# [1.3.0](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/compare/v1.2.0...v1.3.0) (2026-02-26)


### Features

* **door:** added door-service dependency to docker compose ([a3bd3a4](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/a3bd3a41e5196e00b574636ef19af5d2d95c3db3))
* **door:** application boot ([c9d1be9](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/c9d1be92a1c385f8538478b213d348c87df095d9))
* **door:** dependencies ([3c4dc39](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/3c4dc39d4cce8c01c23e8bac150e8c00030a0f93))
* **door:** dockerfile and build script to build the door image ([056f056](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/056f0562a6508eb473e1cdd4930b0070aafabd22))
* **door:** dockerfile and build script to build the door image ([d1ce493](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/d1ce4937916e7d8516d7c6815c17003853f4c9d5))
* **door:** implemented DTOs ([ef01d3b](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/ef01d3b484d34d43c481c1ca81e2237d3703a684))
* **door:** implemented gateService and gateController ([bbc9633](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/bbc9633c3160ccbb60c8bbda1e772b0f91f14d29))
* **door:** implemented the entities ([5153792](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/51537925d440d1f1c1cf72bd7987c60139f51696))

# [1.2.0](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/compare/v1.1.2...v1.2.0) (2026-02-24)


### Bug Fixes

* **back:** correction de certains tests unitaires liés au cart et au forfaitOffer ([9bdaf6d](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/9bdaf6d7249481cc8919f8e77139dfa5609221fa))
* **back:** correction de tests unitaires après rebase ([f30a5d6](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/f30a5d60b5217a8f5a53dd5ee03c3c7f00f6a3e9))
* **back:** rebase de origin main dans main local ([0994816](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/0994816d08a4be4dd296a753ca21819c3db59b09))


### Features

* ajout d'un controlleur pour consulter catalogue et visualiser forfaits existants + ForfaitDTO ([08822e1](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/08822e16fcf487a0cb90e5c0f80df6c5f6755b05))
* **back:** added files I needed ([b2c0704](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/b2c0704fddfd35f400c37d246bdbd210f99ed0ff))
* **back:** added validate & removeItem on entity and component Cart ([6f9d09f](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/6f9d09f8f5a279c40517c63f3e022c34e4a87937))
* **back:** components interacting with forfaitoffer & product repos ([2e47164](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/2e47164fe9b1999d2cd0162f3232b46fd7400f12))
* **back:** endpoints for each method in cart interfaces ([fb15987](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/fb159872e37b26467a30f2178195eea67e2f74e4))
* **back:** entity creation ([fd33bd5](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/fd33bd51de03ccb1941459a7c7681e0b2e942fe8))
* **back:** enums & exceptions creation ([314aa1b](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/314aa1b35a1a6c19b0413273fa39253832b88158))
* **back:** forfaitOfferRepository & ProductRepository implementation ([fbd2850](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/fbd2850aa2eb908225b51a8de77a2b9152dbf936))
* **back:** implementation of the order class ([825a2bd](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/825a2bdb64c99d95b4bd7e03b0f7220790c582b8))
* **back:** implementation of the orderer component ([99469b9](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/99469b9ad1e285e1be520a1a5f6a1f01b534791a))
* **back:** informations of the forfait are now wrapped in a forfaitOffer ([760ddb4](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/760ddb4b9c14c23ce5f8526698c27521261df1ae))
* **back:** orders are stored directly in the customer ([5b67424](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/5b674243738609209d4fe288e951f65870918f1c))
* changement de nom de mapToDTO() ([0062633](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/0062633d1a0af6285ad6ed9270dcce724fbc4c2a))
* implémentation de ForfaitRegistry ([6a0421a](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/6a0421ac876da2040a6bc057ebafff1f7988a65c))
* implémentation des Forfaits et des interfaces pour la registry ([e0a5769](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/e0a5769ca3378fdd8d3878fbe5caa81e26c5b947))
* tests unitaires pour les forfaits ([ed2bcad](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/ed2bcad174f51dc51ee9eae8644b1291120bc1c7))

## [1.1.2](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/compare/v1.1.1...v1.1.2) (2026-02-17)


### Bug Fixes

* **github:** fix commitlint error ([2c994ae](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/2c994ae06845b27c8a67edbd19bf399b22290914))

## 1.1.1 (2026-02-09)


### Bug Fixes

* convert submodule to regular files ([446fcca](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/446fcca2e2164db88c1516e388f6bb6478ecc9ab))
* correction du gitignore (exclude node_modules, include lockfile) ([73689c5](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/73689c54b15caa2ec63860db6b19b480870e93e4))
* **github:** add cd backend command in workflows ([4513b0b](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/4513b0be412b60d9feffd3f9565e8f5c74456bff))
* **github:** add checkout in each workflows ([43e131c](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/43e131c4afaf03b32dae82c060a11f3ef6ca46f0))
* **github:** add test-runner.yml ([6765467](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/6765467fd92d444c07aadf748b60042803396523))
* **github:** debug github identity ([4e0ca64](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/4e0ca64e7d5a37d481fd0aee72efee1a2ab4eeb1))
* **github:** final fix ([f4bfeb6](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/f4bfeb621c18b9b8ba0467814c4a334487ce9f74))
* **github:** fix maven ([8e0cacd](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/8e0cacd9348df86acd557a64e86296d0b0302858))
* **github:** fix release (debugging) ([8d71e92](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/8d71e9260b95ef2be781810545e28250d35b02a4))
* **github:** fix release (debugging).2 ([94efc1a](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/94efc1a6b153b625f77cfeec28b24a808995be53))
* **github:** fix release (debugging).3 ([c2cc1f5](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/c2cc1f524f16c90f21e6b03fb0e1f9406373a8ba))
* **github:** fix release-it ([ff21ce0](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/ff21ce0492b1713f33a8543f0684ad2f727f0f22))
* **github:** fix release-it.json ([d10fb8c](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/d10fb8c92ef40530d75a097bb238e33d89465c78))
* **github:** fix test-runner ([b2d5bd4](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/b2d5bd4855dead76cfc9b800646f7b0939061688))
* **github:** fix test-runner ([83ace5b](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/83ace5b27f10e0806964a8eef56be3b6e2ade62a))
* **github:** fix test-runner.2 ([2b6e309](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/2b6e309d4976df13a94906a66b5b06c08f76176a))
* suppression du dossier SkiMaster et correction de l'arborescence pour le CI ([917b938](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/917b938865d77d552ecd6a16096e4458dbc98467))


### Features

* Ajout de Husky et Commitlin ([f34a7df](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/f34a7df02e15abc9319edd0ead1bdee88d78002e))
* commit qui marche pas ([4c62859](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/4c6285990475f00d9c6e66611b6851cfc7cba2b3))
* **github:** switch to self-hosted and fix release-it ([d85a149](https://github.com/pns-isa-devops/isa-devops-25-26-isa-25-26-team-k/commit/d85a149e78ad62685f6a209214f982d4bd49482a))
