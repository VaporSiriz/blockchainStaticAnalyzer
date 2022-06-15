pragma solidity^0.6.0;

contract FrozenEther {

    mapping(address => uint256) public balances;

    function deposit(address _to) public payable {
        (bool success, ) = _to.delegatecall(msg.data);
        require(success, "Failed");
    }

    receive() external payable {
        balances[msg.sender] += msg.value;
    }

    fallback() external payable {
        balances[msg.sender] += msg.value;
    }

    function init(address _to) public {
        balances[msg.sender] = 0;
    }
}
