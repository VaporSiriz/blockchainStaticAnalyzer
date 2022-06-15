pragma solidity^0.4.25;

contract FrozenEther {

    mapping(address => uint256) public balances;

    function() public payable {
        balances[msg.sender] += msg.value;
    }
}
