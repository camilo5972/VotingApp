// SPDX-License-Identifier: MIT
pragma solidity ^0.8.11;

contract Voting {
    address public admin; // Dirección del administrador (creador del contrato)
    bool public votingClosed = false; // Indica si las votaciones están cerradas

    struct Candidate {
        uint id;
        string name;
        uint voteCount;
    }

    mapping(uint => Candidate) public candidates;
    mapping(address => bool) public hasVoted; // Almacena si un usuario ya votó

    uint public candidatesCount; // Almacena el número de candidatos

    modifier onlyAdmin() {
        require(msg.sender == admin, unicode"¡Solo el administrador puede realizar esta acción!");
        _;
    }

    modifier votingOpen() {
        require(!votingClosed, unicode"¡Las votaciones están cerradas!");
        _;
    }

    constructor() {
        admin = msg.sender; // El creador del contrato es el administrador
    }

    function addCandidate(uint _id, string memory _name) public onlyAdmin votingOpen {
        require(candidates[_id].id == 0, "El candidato ya existe");
        candidates[_id] = Candidate(_id, _name, 0);
        candidatesCount++; // Incrementa el número de candidatos
    }

    function vote(uint _candidateId) public votingOpen {
        require(!hasVoted[msg.sender], "Ya has votado");
        require(candidates[_candidateId].id != 0, "El candidato no existe");
        candidates[_candidateId].voteCount++;
        hasVoted[msg.sender] = true;
    }

    function closeVoting() public onlyAdmin {
        votingClosed = true; // Cierra las votaciones
    }

    function getCandidates() public view returns (uint[] memory, string[] memory, uint[] memory) {
        uint[] memory ids = new uint[](candidatesCount);
        string[] memory names = new string[](candidatesCount);
        uint[] memory voteCounts = new uint[](candidatesCount);

        for (uint i = 0; i < candidatesCount; i++) {
            Candidate storage candidate = candidates[i];
            ids[i] = candidate.id;
            names[i] = candidate.name;
            voteCounts[i] = candidate.voteCount;
        }

        return (ids, names, voteCounts);
    }
}