let
  pkgs = import <nixpkgs> {};
in
  pkgs.mkShell {
    nativeBuildInputs = with pkgs; [
      jdk17
      maven
    ];
  }
