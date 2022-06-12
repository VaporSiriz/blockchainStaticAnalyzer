@echo off
solc\%1\solc.exe --ast-compact-json solidity_examples\%2 > solidity_examples\%2.ast