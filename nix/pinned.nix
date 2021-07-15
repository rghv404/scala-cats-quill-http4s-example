{
  nixpkgs = fetchTarball {
    name   = "nixos-unstable-2021-02-21";
    url    = "https://github.com/NixOS/nixpkgs/archive/9816b99e71c.tar.gz";
    sha256 = "1dpz36i3vx0c1wmacrki0wsf30if8xq3bnj71g89rsbxyi87lhcm";
  };
}