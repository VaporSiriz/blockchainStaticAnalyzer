pragma solidity^0.5.1;

contract ownedContract {
    uint a;
    function get() public view returns(uint) {
        return a;
    }
}

contract BaseNo1 is ownedContract {
    function set() public {
        a = 1;
    }
}

contract BaseNo2 is ownedContract {
    function set() public {
        a = 2;
    }
}

contract Final is BaseNo1, BaseNo2 { }