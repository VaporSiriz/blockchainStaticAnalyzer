pragma solidity^0.4.25;

contract FrozenEther {

    mapping(address => uint256) public balances;

    function() public payable {
        balances[msg.sender] += msg.value;
    }

    function initSender(address _to) public {
        balances[msg.sender] = 0;
    }

    function deposit(address _to) public payable {
        (bool success, ) = _to.delegatecall(msg.data);
        require(success, "Failed");
    }

}
